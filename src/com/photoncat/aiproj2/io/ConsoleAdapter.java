package com.photoncat.aiproj2.io;

import com.photoncat.aiproj2.game.FlippedBoard;
import com.photoncat.aiproj2.game.SimpleBoard;
import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This is a console adapter for development and TA evaluation.
 *
 * There's only one game. All ids are ignored.
 */
public class ConsoleAdapter implements Adapter{
    private MutableBoard game = null;
    private boolean crossPlaying = false;
    private boolean needsFlip = false;

    @Override
    public int createGame(int otherTeamId, int boardSize, int target) {
        game = new SimpleBoard(boardSize, target);
        crossPlaying = false;
        needsFlip = false;
        return 0;
    }

    @Override
    public void joinGame(int gameId) {
        if (game == null) {
            game = new SimpleBoard(12, 6);
        }
        crossPlaying = true;
        needsFlip = true;
    }

    @Override
    public void moveAt(int gameId, Move move) {
        game.putPiece(move);
        crossPlaying = true;
    }

    @Override
    public Board.PieceType getLastMove(int gameId) {
        if (crossPlaying) {
            getConsoleInput();
            crossPlaying = false;
        }
        return Board.PieceType.CIRCLE;
    }

    @Override
    public Board getBoard(int gameId) {
        if (needsFlip) {
            return new FlippedBoard(game);
        }
        return game;
    }

    private void getConsoleInput() {
        String board = game.toString();
        String[] rows = board.split("\n");
        for (int i = 0; i < rows.length; ++i) {
            System.out.println(rows[i] + " <- " + i);
        }
        for (int i = 0; i < rows.length; i += 2) {
            System.out.print(String.format("%1$2d", i));
        }
        System.out.println();
        System.out.print(" ");
        for (int i = 1; i < rows.length; i += 2) {
            System.out.print(String.format("%1$2d", i));
        }
        System.out.println();
        System.out.println("  -> y");
        System.out.println("|\nv\nx");
        // Get console input.
        int x;
        int y;
        int size = game.getSize();
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Input x and y: ");
            x = scanner.nextInt();
            y = scanner.nextInt();
        } while (!game.putPiece(new Move(x, y)));
        System.out.println(game.toString());
        System.out.println("+++++++++++++++++++++++++++++++++++");
    }
}
