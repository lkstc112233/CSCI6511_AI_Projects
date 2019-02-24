package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;

public class SimpleBoard implements Board {
    private PieceType[][] board;
    private int m;
    private PieceType winner = null;
    private PieceType next = PieceType.CIRCLE;
    public SimpleBoard(int size, int m) {
        board = new PieceType[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                board[i][j] = PieceType.NONE;
            }
        }
        this.m = m;
    }

    @Override
    public int getSize() {
        return board.length;
    }

    @Override
    public int getM() {
        return m;
    }

    @Override
    public PieceType getPiece(int x, int y) {
        if (x < 0 || x >= getSize() || y < 0 || y >= getSize()) {
            return null;
        }
        return board[x][y];
    }

    @Override
    public PieceType wins() {
        if (winner == null) {
            return PieceType.NONE;
        }
        return winner;
    }

    @Override
    public boolean gameover() {
        return winner != null;
    }

    private interface CheckIsSame {
        boolean check(int offset, int x, int y);
    }

    private CheckIsSame[] isSames = new CheckIsSame[] {
            (offset, x, y) -> (x + offset >= 0) && (x + offset < getSize()) && (board[x + offset][y] == next),
            (offset, x, y) -> (y + offset >= 0) && (y + offset < getSize()) && (board[x][y + offset] == next),
            (offset, x, y) -> (x + offset >= 0) && (x + offset < getSize()) && (y + offset >= 0) && (y + offset < getSize()) && (board[x + offset][y + offset] == next),
            (offset, x, y) -> (x - offset >= 0) && (x - offset < getSize()) && (y + offset >= 0) && (y + offset < getSize()) && (board[x - offset][y + offset] == next),
    };

    @Override
    public boolean putPiece(int x, int y) {
        if (gameover() ||
                x < 0 || x >= getSize() || y < 0 || y >= getSize() ||
                board[x][y] != PieceType.NONE) {
            return false;
        }
        board[x][y] = next;
        // Check win condition.
        for (var checker : isSames) {
            int continuous = 0;
            int offset = 0;
            while (checker.check(offset, x, y)) {
                continuous += 1;
                offset += 1;
            }
            offset = x - 1;
            while (checker.check(offset, x, y)) {
                continuous += 1;
                offset -= 1;
            }
            if (continuous > getM()) {
                winner = next;
                break;
            }
        }
        next = next == PieceType.CIRCLE ? PieceType.CROSS : PieceType.CIRCLE;
        return true;
    }

    @Override
    public void takeBack() {
        throw new UnsupportedOperationException("This method is not implemented yet.");
    }
}
