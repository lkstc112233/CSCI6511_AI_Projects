package com.photoncat.aiproj2.interfaces;

/**
 * Interface of the board. Supports getting piece, and checking if a player wins.
 */
public interface Board {
    enum PieceType {
        CROSS{
            @Override
            public PieceType flipPiece() {
                return CIRCLE;
            }
        },
        CIRCLE{
            @Override
            public PieceType flipPiece() {
                return CROSS;
            }
        },
        NONE {
            @Override
            public PieceType flipPiece() {
                return NONE;
            }
        },;
        public abstract PieceType flipPiece();
    }

    /**
     * @return size of the board, aka n.
     */
    int getSize();

    /**
     * @return How many pieces needed to be consecutive to win.
     */
    int getM();

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
}
