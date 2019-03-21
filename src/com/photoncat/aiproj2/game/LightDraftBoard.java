package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;

import java.util.HashMap;

/**
 * This draft board only takes one step when constructed.
 */
public class LightDraftBoard implements Board {
    private final Board board;
    private final Move move;
    private final PieceType piece;
    private final boolean won;
    private final boolean full;
    private final PieceType[][] cached;
    private final int size;
    private final int m;

    public LightDraftBoard(Board base, Move move, PieceType type) {
        board = base;
        this.move = move;
        piece = type;
        size = board.getSize();
        cached = new PieceType[size][size];
        m = board.getM();
        won = WinningChecker.winningCheck(this, move.x, move.y, type);
        boolean full = true;
        escape:
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
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
        return size;
    }

    @Override
    public int getM() {
        return m;
    }

    @Override
    public PieceType getPiece(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return null;
        }
        if (cached[x][y] != null) {
            return cached[x][y];
        }
        if (move.x == x && move.y == y) {
            return cached[x][y] = piece;
        }
        return cached[x][y] = board.getPiece(x, y);
    }

    @Override
    public PieceType wins() {
        return won ? piece : PieceType.NONE;
    }

    @Override
    public boolean gameover() {
        return won || full;
    }

    @Override
    public String toString() {
        return Board.toString(this);
    }
}
