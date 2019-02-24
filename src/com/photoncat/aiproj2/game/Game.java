package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.io.Adapter;

/**
 * This is the gaming thread. Each game is handled in a separate thread to support functions of polling.
 */
public class Game extends Thread{
    private Adapter ioAdapter;
    private int gameId;

    /**
     * Host a game with another team.
     */
    public Game(Adapter ioAdapter, int otherTeamId, int boardSize, int target) {
        this.ioAdapter = ioAdapter;
        gameId = ioAdapter.createGame(otherTeamId, boardSize, target);
    }

    /**
     * Join another game.
     */
    public Game(Adapter ioAdapter, int gameId) {
        this.ioAdapter = ioAdapter;
        this.gameId = gameId;
    }

    @Override
    public void run() {
        Board board = ioAdapter.getBoard(gameId);
        while (board != null && !board.gameover()) {
            // TODO: Decide where to move.
            int x = 0;
            int y = 0;
            ioAdapter.moveAt(gameId, x, y);
            do {
                board = ioAdapter.getBoard(gameId);
            } while (board == null);
        }
    }
}
