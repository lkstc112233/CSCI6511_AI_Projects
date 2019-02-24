package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;

public class SimpleBoardTest {
    public static void main(String[] args) {
        // Test based on a 3x3 board. aka classic tic-tac-toe
        Board simpleBoard = new SimpleBoard(3,3);
        if (!simpleBoard.putPiece(1, 1)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Cannot replace any placed position.
        if (simpleBoard.putPiece(1, 1)) throw new AssertionError();  // X
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 0)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 2)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // O, wins
        if (simpleBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
        System.out.println("All test passed");
    }
}
