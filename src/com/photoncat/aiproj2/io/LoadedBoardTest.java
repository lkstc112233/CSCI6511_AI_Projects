package com.photoncat.aiproj2.io;

import com.photoncat.aiproj2.interfaces.Board;

public class LoadedBoardTest {
    public static void main(String[] args) {
        LoadedBoard board = new LoadedBoard("---\n-O-\n-X-\n" , 3);
        board.setLastMove(2,1, Board.PieceType.CROSS);
        if (board.getPiece(0, 0) != Board.PieceType.NONE) throw new AssertionError();
        if (board.getPiece(0, 1) != Board.PieceType.NONE) throw new AssertionError();
        if (board.getPiece(0, 2) != Board.PieceType.NONE) throw new AssertionError();
        if (board.getPiece(1, 0) != Board.PieceType.NONE) throw new AssertionError();
        if (board.getPiece(1, 1) != Board.PieceType.CIRCLE) throw new AssertionError();
        if (board.getPiece(1, 2) != Board.PieceType.NONE) throw new AssertionError();
        if (board.getPiece(2, 0) != Board.PieceType.NONE) throw new AssertionError();
        if (board.getPiece(2, 1) != Board.PieceType.CROSS) throw new AssertionError();
        if (board.getPiece(2, 2) != Board.PieceType.NONE) throw new AssertionError();
        if (!board.putPiece(1, 2)) throw new AssertionError(); // O
        if (board.getPiece(1, 2) != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!board.toString().equals("---\n-OO\n-X-\n")) throw new AssertionError();
        System.out.println("All test passed.");
    }
}
