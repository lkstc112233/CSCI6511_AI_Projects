package com.photoncat.aiproj2.interfaces;

/**
 * Heuristics interface. Takes a board, returns a heuristic value.
 */
public interface Heuristics {
    /**
     * @return a heuristic value. Should be {@link Integer#MIN_VALUE} if the player loses,
     * and {@link Integer#MAX_VALUE} if the player wins.
     */
    int heuristic(Board board);
}
