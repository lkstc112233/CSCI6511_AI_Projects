package com.photoncat.aiproj2.io;

import com.photoncat.aiproj2.game.SimpleBoard;
import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This is a console adapter for development.
 */
public class ConsoleAdapter implements Adapter{
    private Map<Integer, MutableBoard> games = new HashMap<>();
    private Map<Integer, Boolean> crossPlaying = new HashMap<>();

    @Override
    public int createGame(int otherTeamId, int boardSize, int target) {
        games.put(0, new SimpleBoard(boardSize, target));
        crossPlaying.put(0, false);
        return 0;
    }

    @Override
    public void moveAt(int gameId, Move move) {
        games.get(gameId).putPiece(move);
    }

    @Override
    public Board.PieceType getLastMove(int gameId) {
        return crossPlaying.get(gameId) ? Board.PieceType.CIRCLE : Board.PieceType.CROSS;
    }

    @Override
    public Board getBoard(int gameId) {
        var board = games.get(gameId);
        if (!crossPlaying.get(gameId)) {
            crossPlaying.put(gameId, true);
        } else {
            // Get console input.
            System.out.println(board.toString());
            System.out.println("Input x and y: ");
            int x;
            int y;
            Scanner scanner = new Scanner(System.in);
            x = scanner.nextInt();
            y = scanner.nextInt();
            board.putPiece(new Move(x, y));
        }
        return board;
    }
}
