package com.photoncat.aiproj2.io;

import com.photoncat.aiproj2.interfaces.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a console adapter for development.
 */
public class ConsoleAdapter implements Adapter{
    Map<Integer, Board> games = new HashMap<>();
    @Override
    public int createGame(int otherTeamId, int boardSize, int target) {
        return 0;
    }

    @Override
    public void moveAt(int gameId, int x, int y) {

    }

    @Override
    public Board.PieceType getLastMove(int gameId) {
        return null;
    }

    @Override
    public Board getBoard(int gameId) {
        return null;
    }
}
