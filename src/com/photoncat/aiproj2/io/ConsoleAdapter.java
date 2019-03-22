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
    }

    @Override
    public Board.PieceType getLastMove(int gameId) {
        Board.PieceType result = crossPlaying ? Board.PieceType.CIRCLE : Board.PieceType.CROSS;
        if (needsFlip) {
            return result.flipPiece();
        }
        return result;
    }

    @Override
    public Board getBoard(int gameId) {
        if (!crossPlaying) {
            crossPlaying = true;
        } else {
            // Get console input.
            System.out.println(game.toString());
            System.out.println("  -> y");
            System.out.println("|\nv\nx");
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
        if (needsFlip) {
            return new FlippedBoard(game);
        }
        return game;
    }
}
