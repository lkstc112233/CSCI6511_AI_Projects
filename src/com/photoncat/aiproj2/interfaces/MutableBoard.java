package com.photoncat.aiproj2.interfaces;

/**
 * A board that supports modifying. Supports putting a piece onto the board, and taking back a move.
 */
public interface MutableBoard extends Board {
    /**
     * Puts a piece at position (x, y).
     * @return true if succeed, false if the cell is already occupied.
     */
    boolean putPiece(Move move);

    /**
     * Takes back one move from {@link MutableBoard#putPiece(Move)}.
     */
    void takeBack();
}
