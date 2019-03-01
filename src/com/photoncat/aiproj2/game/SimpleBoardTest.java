package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.MutableBoard;

public class SimpleBoardTest {
    public static void main(String[] args) {
        test3x3();
        test5x5();
        testDraw();
        testTakeBack();
        testTakeBackFirstDraw();
        System.out.println("All test passed");
    }

    private static void test3x3() {
        // Test based on a 3x3 board. aka classic tic-tac-toe
        MutableBoard simpleBoard = new SimpleBoard(3,3);
        if (!simpleBoard.putPiece(1, 1)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Cannot replace any placed position.
        if (simpleBoard.putPiece(1, 1)) throw new AssertionError();  // X
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("-X-\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 0)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("OX-\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 2)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("OX-\n-OX\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // O, wins
        if (!simpleBoard.toString().equals("OX-\n-OX\n--O\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
    }

    private static void testTakeBack() {
        // Test based on a 3x3 board. aka classic tic-tac-toe
        MutableBoard simpleBoard = new SimpleBoard(3,3);
        if (!simpleBoard.putPiece(1, 1)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Cannot replace any placed position.
        if (simpleBoard.putPiece(1, 1)) throw new AssertionError();  // X
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("-X-\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Takes back last step
        simpleBoard.takeBack();
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Place on a different spot
        if (!simpleBoard.putPiece(1, 0)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("---\nXO-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // O now can be put on the last spot.
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("-O-\nXO-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 2)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("-O-\nXOX\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 1)) throw new AssertionError(); // O, wins
        if (!simpleBoard.toString().equals("-O-\nXOX\n-O-\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
        // Now take that back
        simpleBoard.takeBack();
        if (!simpleBoard.toString().equals("-O-\nXOX\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("-O-\nXOX\n--O\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 0)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("XO-\nXOX\n--O\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 2)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("XOO\nXOX\n--O\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 0)) throw new AssertionError(); // X, wins
        if (!simpleBoard.toString().equals("XOO\nXOX\nX-O\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.CROSS) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
    }

    private static void testDraw() {
        // Test based on a 3x3 board. aka classic tic-tac-toe
        MutableBoard simpleBoard = new SimpleBoard(3,3);
        if (!simpleBoard.putPiece(1, 1)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 0)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("X--\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 0)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("X--\nOO-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 2)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("X--\nOOX\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("XO-\nOOX\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 1)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("XO-\nOOX\n-X-\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 0)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("XO-\nOOX\nOX-\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 2)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("XOX\nOOX\nOX-\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // O, draws
        if (!simpleBoard.toString().equals("XOX\nOOX\nOXO\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
    }

    private static void testTakeBackFirstDraw() {
        // Test based on a 3x3 board. aka classic tic-tac-toe
        MutableBoard simpleBoard = new SimpleBoard(3,3);
        // Nothing happens when take back from zeroth step.
        simpleBoard.takeBack();
        if (!simpleBoard.putPiece(1, 1)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 0)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("X--\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 0)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("X--\nOO-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 2)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("X--\nOOX\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("XO-\nOOX\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 1)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("XO-\nOOX\n-X-\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 0)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("XO-\nOOX\nOX-\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 2)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("XOX\nOOX\nOX-\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // O, draws
        if (!simpleBoard.toString().equals("XOX\nOOX\nOXO\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
        simpleBoard.takeBack();
        if (!simpleBoard.toString().equals("XOX\nOOX\nOX-\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        simpleBoard.takeBack();
        if (!simpleBoard.toString().equals("XO-\nOOX\nOX-\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("XO-\nOOX\nOXX\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 2)) throw new AssertionError(); // O, wins
        if (!simpleBoard.toString().equals("XOO\nOOX\nOXX\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
    }

    private static void test5x5() {
        // Test based on a 5x5 board.
        MutableBoard simpleBoard = new SimpleBoard(5,4);
        if (!simpleBoard.putPiece(1, 1)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("-----\n-O---\n-----\n-----\n-----\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 1)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("-X---\n-O---\n-----\n-----\n-----\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(0, 0)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("OX---\n-O---\n-----\n-----\n-----\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(1, 2)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("OX---\n-OX--\n-----\n-----\n-----\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(3, 3)) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("OX---\n-OX--\n-----\n---O-\n-----\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 3)) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("OX---\n-OX--\n---X-\n---O-\n-----\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        if (!simpleBoard.putPiece(2, 2)) throw new AssertionError(); // O, wins
        if (!simpleBoard.toString().equals("OX---\n-OX--\n--OX-\n---O-\n-----\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
        // After a player wins, no one can put anymore pieces.
        if (simpleBoard.putPiece(4, 4)) throw new AssertionError(); // No one puts any more pieces.
        if (!simpleBoard.toString().equals("OX---\n-OX--\n--OX-\n---O-\n-----\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!simpleBoard.gameover()) throw new AssertionError();
    }
}
