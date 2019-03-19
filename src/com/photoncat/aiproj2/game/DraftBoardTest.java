package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;

public class DraftBoardTest {
    public static void main(String[] args) {
        test3x3();
        testTakeBack();
        System.out.println("All test passed");
    }

    private static void test3x3() {
        // Test based on a 3x3 board. aka classic tic-tac-toe
        MutableBoard simpleBoard = new SimpleBoard(3,3);
        if (!simpleBoard.putPiece(new Move(1, 1))) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Cannot replace any placed position.
        if (simpleBoard.putPiece(new Move(1, 1))) throw new AssertionError();  // X
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (!simpleBoard.putPiece(new Move(0, 1))) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("-X-\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Create a new draft board.
        MutableBoard draftBoard = new DraftBoard(simpleBoard, Board.PieceType.CIRCLE);
        // Cannot replace any placed position.
        if (draftBoard.putPiece(new Move(1, 1))) throw new AssertionError();  // O
        if (!draftBoard.toString().equals("-X-\n-O-\n---\n")) throw new AssertionError();
        if (!draftBoard.putPiece(new Move(0, 0))) throw new AssertionError(); // O
        if (!draftBoard.toString().equals("OX-\n-O-\n---\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        if (!draftBoard.putPiece(new Move(1, 2))) throw new AssertionError(); // X
        if (!draftBoard.toString().equals("OX-\n-OX\n---\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        if (!draftBoard.putPiece(new Move(2, 2))) throw new AssertionError(); // O, wins
        if (!draftBoard.toString().equals("OX-\n-OX\n--O\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!draftBoard.gameover()) throw new AssertionError();
    }

    private static void testTakeBack() {
        // Test based on a 3x3 board. aka classic tic-tac-toe
        MutableBoard simpleBoard = new SimpleBoard(3,3);
        if (!simpleBoard.putPiece(new Move(1, 1))) throw new AssertionError(); // O
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Cannot replace any placed position.
        if (simpleBoard.putPiece(new Move(1, 1))) throw new AssertionError();  // X
        if (!simpleBoard.toString().equals("---\n-O-\n---\n")) throw new AssertionError();
        if (!simpleBoard.putPiece(new Move(0, 1))) throw new AssertionError(); // X
        if (!simpleBoard.toString().equals("-X-\n-O-\n---\n")) throw new AssertionError();
        if (simpleBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (simpleBoard.gameover()) throw new AssertionError();
        // Create a new draft board.
        MutableBoard draftBoard = new DraftBoard(simpleBoard, Board.PieceType.CIRCLE);
        // Cannot takes back last step since the draftBoard didn't have that step recorded.
        draftBoard.takeBack();
        if (!draftBoard.toString().equals("-X-\n-O-\n---\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        // Place on a different spot
        if (!draftBoard.putPiece(new Move(1, 0))) throw new AssertionError(); // X
        if (!draftBoard.toString().equals("-X-\nOO-\n---\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        // Now take that back.
        draftBoard.takeBack();
        if (!draftBoard.toString().equals("-X-\n-O-\n---\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        // O now can be put on another spot.
        if (!draftBoard.putPiece(new Move(2, 2))) throw new AssertionError(); // O
        if (!draftBoard.toString().equals("-X-\n-O-\n--O\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        if (!draftBoard.putPiece(new Move(1, 2))) throw new AssertionError(); // X
        if (!draftBoard.toString().equals("-X-\n-OX\n--O\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        if (!draftBoard.putPiece(new Move(0, 0))) throw new AssertionError(); // O, wins
        if (!draftBoard.toString().equals("OX-\n-OX\n--O\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!draftBoard.gameover()) throw new AssertionError();
        // Now take that back
        draftBoard.takeBack();
        if (!draftBoard.toString().equals("-X-\n-OX\n--O\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        if (!draftBoard.putPiece(new Move(2, 1))) throw new AssertionError(); // O
        if (!draftBoard.toString().equals("-X-\n-OX\n-OO\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        if (!draftBoard.putPiece(new Move(0, 2))) throw new AssertionError(); // X
        if (!draftBoard.toString().equals("-XX\n-OX\n-OO\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        if (!draftBoard.putPiece(new Move(1, 0))) throw new AssertionError(); // O
        if (!draftBoard.toString().equals("-XX\nOOX\n-OO\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        if (!draftBoard.putPiece(new Move(0, 0))) throw new AssertionError(); // X, wins
        if (!draftBoard.toString().equals("XXX\nOOX\n-OO\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.CROSS) throw new AssertionError();
        if (!draftBoard.gameover()) throw new AssertionError();
    }
}
