package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;

public class SimpleBoardTest {
    public static void main(String[] args) {
        test3x3();
        test5x5();
        testDraw();
        testTakeBack();
        System.out.println("All test passed");
    }

    private static void test3x3() {
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
    }

    private static void testTakeBack() {
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
        // Takes back last step
        simpleBoard.takeBack();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Place on a different spot
        if (!simpleBoard.putPiece(1, 0)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // O now can be put on the last spot.
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 2)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 1)) throw new AssertionError(); // O, wins
        if (simpleBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
        // Now take that back
        simpleBoard.takeBack();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 0)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 2)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 0)) throw new AssertionError(); // X, wins
        if (simpleBoard.wins() != Board.PieceType.CROSS) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
    }

    private static void testDraw() {
        // Test based on a 3x3 board. aka classic tic-tac-toe
        Board simpleBoard = new SimpleBoard(3,3);
        if (!simpleBoard.putPiece(1, 1)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 0)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 0)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 2)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 1)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 0)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 2)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // O, draws
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
    }

    private static void test5x5() {
        // Test based on a 5x5 board.
        Board simpleBoard = new SimpleBoard(5,4);
        if (!simpleBoard.putPiece(1, 1)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 0)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 2)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(3, 3)) throw new AssertionError(); // O
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 3)) throw new AssertionError(); // X
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // O, wins
        if (simpleBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
    }
}
