package com.photoncat.aiproj2.interfaces;

/**
 * Interface of the board. Supports getting piece, checking if a player wins, and putting a piece onto the board.
 */
public interface Board {
    enum PieceType {
        CROSS,
        CIRCLE,
        NONE,
    }

    /**
     * @return size of the board, aka n.
     */
    int getSize();

    /**
     * @return piece at the given position
     */
    PieceType getPiece(int x, int y);

    /**
     * @return {@link PieceType#CROSS} if Cross wins, {@link PieceType#CIRCLE} if Circle wins, and {@link PieceType#NONE} if nobody wins so far.
     */
    PieceType wins();

    /**
     * Since the game can be a draw.
     * @return true if the game is over. Get who wins by calling {@link Board#wins}. If no one wins, the game draws.
     */
    boolean gameover();

    /**
     * Puts a piece at position (x, y).
     * @return true if succeed, false if the cell is already occupied.
     */
    boolean putPiece(int x, int y);
}
