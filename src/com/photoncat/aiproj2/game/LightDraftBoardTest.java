package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;

public class LightDraftBoardTest {
    public static void main(String[] args) {
        test3x3();
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
        // Create a new light draft board.
        Board draftBoard = new LightDraftBoard(simpleBoard, new Move(0, 0), Board.PieceType.CIRCLE);
        if (!draftBoard.toString().equals("OX-\n-O-\n---\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        // DraftBoard is immutable.
        draftBoard = new LightDraftBoard(draftBoard, new Move(1, 2), Board.PieceType.CROSS);
        if (!draftBoard.toString().equals("OX-\n-OX\n---\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.NONE) throw new AssertionError();
        if (draftBoard.gameover()) throw new AssertionError();
        draftBoard = new LightDraftBoard(draftBoard, new Move(2, 2), Board.PieceType.CIRCLE); // wins
        if (!draftBoard.toString().equals("OX-\n-OX\n--O\n")) throw new AssertionError();
        if (draftBoard.wins() != Board.PieceType.CIRCLE) throw new AssertionError();
        if (!draftBoard.gameover()) throw new AssertionError();
    }
}
