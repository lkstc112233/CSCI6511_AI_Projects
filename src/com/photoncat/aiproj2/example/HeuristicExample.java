package com.photoncat.aiproj2.example;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Heuristics;

/**
 * A simple Heuristic example. Only counts the available lineups in rows and columns.
 */
public class HeuristicExample implements Heuristics {
    /**
     * A helper class to count continuously pieces.
     */
    private static class ContinuouslyCounter {
        private int continuously = 0;
        public int setPiece(Board.PieceType piece) {
            int result = 0;
            if (piece == Board.PieceType.CIRCLE) {
                if (continuously >= 0) {
                    continuously += 1;
                } else {
                    result = continuously;
                    continuously = 1;
                }
            } else if (piece == Board.PieceType.CROSS) {
                if (continuously <= 0) {
                    continuously -= 1;
                } else {
                    result = continuously;
                    continuously = -1;
                }
            } else if (piece == Board.PieceType.NONE) {
                result = continuously;
                continuously = 0;
            }
            return result;
        }

        public int clearPiece() {
            int result = continuously;
            continuously = 0;
            return result;
        }
    }

    /**
     * @return a heuristic value. Should be {@link Integer#MIN_VALUE} if the player loses,
     * and {@link Integer#MAX_VALUE} if the player wins.
     */
    @Override
    public int heuristic(Board board) {
        if (board.gameover()) {
            if (board.wins() == Board.PieceType.CROSS) {
                return Integer.MIN_VALUE;
            } else if (board.wins() == Board.PieceType.CIRCLE) {
                return Integer.MAX_VALUE;
            } else {
                return 0;
            }
        }
        int heuristic = 0;
        // Count each row and column.
        int size = board.getSize();
        int target = board.getM();
        ContinuouslyCounter row = new ContinuouslyCounter();
        ContinuouslyCounter column = new ContinuouslyCounter();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                heuristic += row.setPiece(board.getPiece(i, j));
                heuristic += column.setPiece(board.getPiece(j, i));
            }
            heuristic += row.clearPiece();
            heuristic += column.clearPiece();
        }
        return heuristic;
    }
}
