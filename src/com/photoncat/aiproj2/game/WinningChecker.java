package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;

class WinningChecker {
    private interface CheckIsSame {
        boolean check(Board board, int offset, int x, int y, Board.PieceType next);
    }

    private static CheckIsSame[] isSames = new CheckIsSame[] {
            (board, offset, x, y, next) -> board.getPiece(x + offset, y) == next,
            (board, offset, x, y, next) -> board.getPiece(x, y + offset) == next,
            (board, offset, x, y, next) -> board.getPiece(x + offset, y + offset) == next,
            (board, offset, x, y, next) -> board.getPiece(x - offset, y + offset) == next,
    };

    static boolean winningCheck(Board board, int x, int y, Board.PieceType next) {
        for (CheckIsSame checker : isSames) {
            int continuous = 0;
            int offset = 0;
            while (checker.check(board, offset, x, y, next)) {
                continuous += 1;
                offset += 1;
            }
            offset = -1;
            while (checker.check(board, offset, x, y, next)) {
                continuous += 1;
                offset -= 1;
            }
            if (continuous >= board.getM()) {
                return true;
            }
        }
        return false;
    }
}
