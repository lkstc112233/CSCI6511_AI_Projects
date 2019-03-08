package com.photoncat.aiproj2.io;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;

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
     * Makes a move.
     */
    void moveAt(int gameId, Move move);

    /**
     * @return who moved last. If no one moved, {@link Board.PieceType#CROSS} will be returned.
     */
    Board.PieceType getLastMove(int gameId);

    /**
     * Gets the board of a given game.
     */
    Board getBoard(int gameId);
}
