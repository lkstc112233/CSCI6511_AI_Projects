package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Heuristics;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;
import com.photoncat.aiproj2.io.Adapter;

import java.util.HashMap;
import java.util.Map;

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

    private Move minMaxSearch(MutableBoard board) {
        // TODO: Decide where to move.
        // Now it's just a one-step maximum search.
        Map<Move, Integer> maximumMap = new HashMap<>();
        for (int x = 0; x < board.getSize(); ++x) {
            for (int y = 0; y < board.getSize(); ++y) {
                Move move = new Move(x, y);
                if (board.putPiece(move)) {
                    if (board.gameover()) { // It can only be our victory, or draw if that's the only move.
                        return move;
                    }
                    maximumMap.put(move, heuristics.heuristic(board));
                    board.takeBack();
                }
            }
        }
        int maximumValue = Integer.MIN_VALUE;
        Move bestMove = null;
        for (var pair: maximumMap.entrySet()) {
            if (maximumValue < pair.getValue()) {
                maximumValue = pair.getValue();
                bestMove = pair.getKey();
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
            var move = minMaxSearch(new DraftBoard(board, Board.PieceType.CIRCLE));
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
