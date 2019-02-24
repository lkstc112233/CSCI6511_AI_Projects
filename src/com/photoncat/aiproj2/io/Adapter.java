package com.photoncat.aiproj2.io;

import com.photoncat.aiproj2.interfaces.Board;

/**
 * An interface handling data communication.
 * Wrapping the web api.
 */
public interface Adapter {
    /**
     * Creates a game.
     * @param otherTeamId The method needs only other team's ID, since our teamId is fixed.
     * @return the game id.
     */
    int createGame(int otherTeamId, int boardSize, int target);

    /**
     * Makes a move at given (x, y).
     */
    void moveAt(int gameId, int x, int y);

    /**
     * Gets the board of a given game.
     */
    Board getBoard(int gameId);
}
