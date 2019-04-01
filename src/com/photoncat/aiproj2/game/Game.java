package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Heuristics;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.io.Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This is the gaming thread. Each game is handled in a separate thread to support functions of polling.
 */
public class Game extends Thread{
    private static final int POLLING_INTERVAL_MILLISECONDS = 5000;
    private Adapter ioAdapter;
    private int gameId;
    private Heuristics heuristics;

    /**
     * Host a game with another team.
     */
    public Game(Adapter ioAdapter, int otherTeamId, int boardSize, int target, Heuristics heuristics) {
        this.ioAdapter = ioAdapter;
        this.heuristics = heuristics;
        gameId = ioAdapter.createGame(otherTeamId, boardSize, target);
    }

    /**
     * Join another game.
     */
    public Game(Adapter ioAdapter, int gameId, Heuristics heuristics) {
        this.ioAdapter = ioAdapter;
        this.heuristics = heuristics;
        this.gameId = gameId;
        ioAdapter.joinGame(gameId);
    }

    public int getGameId() {
        return gameId;
    }

    /**
     * A class holding min-max tree node.
     */
    private class MinMaxNode {
        Move move = null;
        Board board = null;
        int value = 0;
        int minPossibleValue = Integer.MIN_VALUE;
        int maxPossibleValue = Integer.MAX_VALUE;
        MinMaxNode parent = null;
        boolean assign(int value, boolean minLayer) {
            this.value = value;
            if (parent != null) {
                return parent.update(value, !minLayer);
            }
            return true;
        }
        boolean update(int value, boolean minLayer) {
            boolean updated = false;
            if (minLayer) {
                if (maxPossibleValue > value) {
                    maxPossibleValue = value;
                    updated = true;
                }
            } else {
                if (minPossibleValue < value) {
                    minPossibleValue = value;
                    updated = true;
                }
            }
            if (updated && parent != null) {
                parent.update(value, !minLayer);
            }
            return updated;
        }
    }

    private class ExpandTask implements Runnable{
        private final MinMaxNode node;
        private final List<MinMaxNode> secondLayer;
        private final boolean minLayer;
        ExpandTask(MinMaxNode node, List<MinMaxNode> secondLayer, boolean minLayer) {
            this.node = node;
            this.secondLayer = secondLayer;
            this.minLayer = minLayer;
        }
        @Override
        public void run() {
            for (int x = 0; x < node.board.getSize(); ++x) {
                for (int y = 0; y < node.board.getSize(); ++y) {
                    if (node.board.getPiece(x, y) == Board.PieceType.NONE) {
                        Move move = new Move(x, y);
                        Board newBoard = new LightDraftBoard(
                                node.board,
                                move,
                                minLayer ? Board.PieceType.CIRCLE : Board.PieceType.CROSS);
                        MinMaxNode childNode = new MinMaxNode();
                        childNode.move = move;
                        childNode.board = newBoard;
                        childNode.parent = node;
                        int score = heuristics.heuristic(newBoard);
                        synchronized (Game.this) {
                            if (childNode.assign(score, minLayer)) {
                                secondLayer.add(childNode);
                            }
                        }
                    }
                }
            }
        }
    }

    private Move minMaxSearch(Board board) {
        // Decide where to move.
        MinMaxNode root = new MinMaxNode();
        root.board = board;
        List<MinMaxNode> firstLayer = new ArrayList<>();
        List<MinMaxNode> nextLayer;
        for (int x = 0; x < board.getSize(); ++x) {
            for (int y = 0; y < board.getSize(); ++y) {
                if (board.getPiece(x, y) == Board.PieceType.NONE) {
                    Move move = new Move(x, y);
                    Board newBoard = new LightDraftBoard(board, move, Board.PieceType.CIRCLE); // It's always our turn.
                    if (newBoard.gameover()) { // It can only be our victory, or draw if that's the only move.
                        return move;
                    }
                    MinMaxNode node = new MinMaxNode();
                    node.move = move;
                    node.board = newBoard;
                    node.parent = root;
                    node.value = heuristics.heuristic(newBoard);
                    firstLayer.add(node);
                }
            }
        }
        // Expand nodes with a thread pool.
        final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(32);
        boolean isMinLayer = false;
        nextLayer = firstLayer;
        for (int i = 0; i < 2; ++i) {
            List<MinMaxNode> newLayer = new ArrayList<>();
            for (MinMaxNode node : nextLayer) {
                synchronized (this) {
                    ExpandTask newTask = new ExpandTask(node, newLayer, isMinLayer);
                    threadPool.execute(newTask);
                }
            }
            // Wait until all threads are done.
            while (threadPool.getActiveCount() != 0) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isMinLayer = !isMinLayer;
            nextLayer = newLayer;
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(15, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!threadPool.isTerminated()) {
            threadPool.shutdownNow();
        }
        int maximumValue = Integer.MIN_VALUE;
        Move bestMove = null;
        for (MinMaxNode node: firstLayer) {
            if (maximumValue < node.maxPossibleValue) {
                maximumValue = node.maxPossibleValue;
                bestMove = node.move;
            }
        }
        if (bestMove == null) {
            // There's no any valid move.
            bestMove = new Move(0, 0);
        }
        return bestMove;
    }

    @Override
    public void run() {
        while (ioAdapter.getLastMove(gameId) == Board.PieceType.CROSS) {
            // Wait for 5 seconds - As Professor Arora suggested in slack.
            try {
                Thread.sleep(POLLING_INTERVAL_MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Board board = ioAdapter.getBoard(gameId);
        while (board != null && !board.gameover()) {
            Move move = minMaxSearch(new DraftBoard(board, Board.PieceType.CIRCLE));
            ioAdapter.moveAt(gameId, move);
            while (!board.gameover() && ioAdapter.getLastMove(gameId) == Board.PieceType.CROSS) {
                // Wait for 5 seconds - As Professor Arora suggested in slack.
                try {
                    Thread.sleep(POLLING_INTERVAL_MILLISECONDS);
                } catch (InterruptedException e) {
                    // Ignore the interrupted since:
                    // a) There won't be any interruption.
                    // b) Even if there were any, we can simply recheck the status.
                    e.printStackTrace();
                }
            }
            do {
                board = ioAdapter.getBoard(gameId);
            } while (board == null);
        }
    }
}
