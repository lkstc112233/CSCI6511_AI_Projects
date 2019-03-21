package com.photoncat.aiproj2.io;

import com.photoncat.aiproj2.game.FlippedBoard;
import com.photoncat.aiproj2.game.SimpleBoard;
import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a console adapter for development.
 *
 * If the gameId is negative, the pieces are flipped.
 */
public class SelfAdapter implements Adapter {
    private Map<Integer, MutableBoard> games = new HashMap<>();
    private Map<Integer, Boolean> crossPlaying = new HashMap<>();

    @Override
    public int createGame(int otherTeamId, int boardSize, int target) {
        int gameId = games.size() + 1;
        games.put(gameId, new SimpleBoard(boardSize, target));
        crossPlaying.put(gameId, false);
        return gameId;
    }

    @Override
    public void joinGame(int gameId) {}

    @Override
    public void moveAt(int gameId, Move move) {
        System.out.println("Player " + (gameId>0?"Cross":"Circle") + " moving: ");
        if (gameId < 0) {
            gameId = -gameId;
        }
        games.get(gameId).putPiece(move);
        crossPlaying.put(gameId, !crossPlaying.get(gameId));
        System.out.println(games.get(gameId));
    }

    @Override
    public Board.PieceType getLastMove(int gameId) {
        boolean flipped = false;
        if (gameId < 0) {
            gameId = -gameId;
            flipped = true;
        }
        return (crossPlaying.get(gameId) ^ flipped) ? Board.PieceType.CIRCLE : Board.PieceType.CROSS;
    }

    @Override
    public Board getBoard(int gameId) {
        boolean flipped = false;
        if (gameId < 0) {
            gameId = -gameId;
            flipped = true;
        }
        MutableBoard board = games.get(gameId);
        if (flipped) {
            return new FlippedBoard(board);
        }
        return board;
    }
}
