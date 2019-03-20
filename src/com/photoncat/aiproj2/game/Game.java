package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Heuristics;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;
import com.photoncat.aiproj2.io.Adapter;
import com.photoncat.aiproj2.util.OnetimePriorityQueue;

import java.util.ArrayList;
import java.util.List;

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
    }

    /**
     * A class holding min-max tree node.
     */
    private class MinMaxNode {
        Move move = null;
        MutableBoard board = null;
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

    private void expandNode(MinMaxNode node, OnetimePriorityQueue<MinMaxNode> nextLayer, boolean minLayer) {
        for (int x = 0; x < node.board.getSize(); ++x) {
            for (int y = 0; y < node.board.getSize(); ++y) {
                Move move = new Move(x, y);
                if (node.board.putPiece(move)) {
                    MinMaxNode childNode = new MinMaxNode();
                    childNode.move = move;
                    childNode.board = new SimpleBoard(node.board, move);
                    childNode.parent = node;
                    int score = heuristics.heuristic(node.board);
                    childNode.update(score, minLayer);
                    nextLayer.add(childNode, score);
                    node.board.takeBack();
                }
            }
        }
    }

    private Move minMaxSearch(MutableBoard board) {
        // TODO: Decide where to move.
        // Now it's just a one-step maximum search.
        List<MinMaxNode> firstLayer = new ArrayList<>();
        OnetimePriorityQueue<MinMaxNode> maxLayerNodes = new OnetimePriorityQueue<>(OnetimePriorityQueue.compareByMax);
        OnetimePriorityQueue<MinMaxNode> minLayerNodes = new OnetimePriorityQueue<>();
        final int MAXIMUM_NODES_EXPANDED = 3000;
        for (int x = 0; x < board.getSize(); ++x) {
            for (int y = 0; y < board.getSize(); ++y) {
                Move move = new Move(x, y);
                if (board.putPiece(move)) {
                    if (board.gameover()) { // It can only be our victory, or draw if that's the only move.
                        return move;
                    }
                    MinMaxNode node = new MinMaxNode();
                    node.move = move;
                    node.board = new SimpleBoard(board, move);
                    node.minPossibleValue = heuristics.heuristic(board);
                    firstLayer.add(node);
                    maxLayerNodes.add(node, node.minPossibleValue);
                    board.takeBack();
                }
            }
        }
        // Expand nodes
        int expandedCount = 0;
        while (expandedCount < MAXIMUM_NODES_EXPANDED) {
            if (maxLayerNodes.isEmpty() && minLayerNodes.isEmpty()) {
                break;
            }
            if (!maxLayerNodes.isEmpty()) {
                MinMaxNode node = maxLayerNodes.poll().getKey();
                expandNode(node, minLayerNodes, false);
                expandedCount += 1;
            }
            if (!minLayerNodes.isEmpty()) {
                MinMaxNode node = minLayerNodes.poll().getKey();
                expandNode(node, maxLayerNodes, true);
                expandedCount += 1;
            }
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
