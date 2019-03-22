package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Heuristics;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.io.Adapter;
import com.photoncat.aiproj2.util.OnetimePriorityQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        int minPossibleValue = Integer.MIN_VALUE;
        int maxPossibleValue = Integer.MAX_VALUE;
        MinMaxNode parent = null;
        void update(int value, boolean minLayer) {
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
        }
    }

    private class ExpandTask implements Runnable{
        private final MinMaxNode node;
        private final OnetimePriorityQueue<MinMaxNode> nextLayer;
        private final boolean minLayer;
        ExpandTask(MinMaxNode node, OnetimePriorityQueue<MinMaxNode> nextLayer, boolean minLayer) {
            this.node = node;
            this.nextLayer = nextLayer;
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
                                minLayer ? Board.PieceType.CROSS : Board.PieceType.CIRCLE);
                        MinMaxNode childNode = new MinMaxNode();
                        childNode.move = move;
                        childNode.board = newBoard;
                        childNode.parent = node;
                        int score = heuristics.heuristic(newBoard);
                        // cut-offs
                        if (node.parent != null) {
                            if (minLayer) {
                                if (score > node.parent.maxPossibleValue) {
                                    // Beta cut off
                                    break;
                                }
                            } else if (score < node.parent.minPossibleValue) {
                                // Alpha cut off
                                break;
                            }
                        }
                        synchronized (Game.this) {
                            childNode.update(score, minLayer);
                            nextLayer.add(childNode, score);
                        }
                    }
                }
            }
        }
    }

    private Move minMaxSearch(Board board) {
        // Decide where to move.
        List<MinMaxNode> firstLayer = new ArrayList<>();
        OnetimePriorityQueue<MinMaxNode> maxLayerNodes = new OnetimePriorityQueue<>(OnetimePriorityQueue.compareByMax);
        OnetimePriorityQueue<MinMaxNode> minLayerNodes = new OnetimePriorityQueue<>();
        final int MAXIMUM_NODES_EXPANDED = 20000;
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
                    node.minPossibleValue = heuristics.heuristic(newBoard);
                    firstLayer.add(node);
                    maxLayerNodes.add(node, node.minPossibleValue);
                }
            }
        }
        // Expand nodes with a thread pool.
        int expandedCount = 0;
        final ExecutorService threadPool = Executors.newFixedThreadPool(32);
        while (expandedCount < MAXIMUM_NODES_EXPANDED) {
            synchronized (this) {
                if (maxLayerNodes.isEmpty() && minLayerNodes.isEmpty()) {
                    if (threadPool.isTerminated()) {
                        break;
                    }
                    // take a nap and check again.
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (maxLayerNodes.isEmpty() && minLayerNodes.isEmpty()) {
                        break;
                    }
                }
            }
            synchronized (this) {
                if (!maxLayerNodes.isEmpty()) {
                    MinMaxNode node = maxLayerNodes.poll().getKey();
                    ExpandTask newTask = new ExpandTask(node, minLayerNodes, false);
                    threadPool.execute(newTask);
                    expandedCount += 1;
                }
            }
            synchronized (this) {
                if (!minLayerNodes.isEmpty()) {
                    MinMaxNode node = minLayerNodes.poll().getKey();
                    ExpandTask newTask = new ExpandTask(node, maxLayerNodes, true);
                    threadPool.execute(newTask);
                    expandedCount += 1;
                }
            }
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
            if (maximumValue < node.minPossibleValue) {
                maximumValue = node.minPossibleValue;
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
