package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;

/**
 * This draft board only takes one step when constructed.
 */
public class LightDraftBoard implements Board {
    private final Board board;
    private final Move move;
    private final PieceType piece;
    private final boolean won;
    private final boolean full;

    public LightDraftBoard(Board base, Move move, PieceType type) {
        board = base;
        this.move = move;
        piece = type;
        won = WinningChecker.winningCheck(this, move.x, move.y, type);
        boolean full = true;
        escape:
        for (int i = 0; i < board.getSize(); ++i) {
            for (int j = 0; j < board.getSize(); ++j) {
                if (board.getPiece(i, j) == PieceType.NONE) {
                    full = false;
                    break escape;
                }
            }
        }
        this.full = full;
    }

    @Override
    public int getSize() {
        return board.getSize();
    }

    @Override
    public int getM() {
        return board.getM();
    }

    @Override
    public PieceType getPiece(int x, int y) {
        if (move.x == x && move.y == y) {
            return piece;
        }
        return board.getPiece(x, y);
    }

    @Override
    public PieceType wins() {
        return won ? piece : PieceType.NONE;
    }

    @Override
    public boolean gameover() {
        return won || full;
    }
}
