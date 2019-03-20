package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;

/**
 * A simple layer wrapper who flips the board.
 */
public class FlippedBoard implements MutableBoard {
    private MutableBoard board;
    public FlippedBoard(MutableBoard board) {
        this.board = board;
    }

    @Override
    public boolean putPiece(Move move) {
        return board.putPiece(move);
    }

    @Override
    public void takeBack() {
        board.takeBack();
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
        return board.getPiece(x, y).flipPiece();
    }

    @Override
    public PieceType wins() {
        return board.wins().flipPiece();
    }

    @Override
    public boolean gameover() {
        return board.gameover();
    }
}
