package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
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
        PieceType piece = board.getPiece(x, y);
        if (piece != null) {
            return piece.flipPiece();
        }
        return null;
    }

    @Override
    public PieceType wins() {
        PieceType winner = board.wins();
        if (winner != null) {
            return winner.flipPiece();
        }
        return null;
    }

    @Override
    public boolean gameover() {
        return board.gameover();
    }

    @Override
    public String toString() {
        return Board.toString(this);
    }
}
