package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;

import java.util.Stack;

/**
 * A board intended to be used as a draft. Wraps a board.
 */
class DraftBoard extends SimpleBoard {
    private Board board;

    public DraftBoard(Board board, PieceType nextType) {
        super(board.getSize(), board.getM());
        this.board = board;
        this.next = nextType;
        for (int x = 0; x < board.getSize(); ++x) {
            for (int y = 0; y < board.getSize(); ++y) {
                if (super.getPiece(x, y) != PieceType.NONE) {
                    steps += 1;
                }
            }
        }
    }

    @Override
    public PieceType getPiece(int x, int y) {
        PieceType type = board.getPiece(x, y);
        if (type != PieceType.NONE) {
            return type;
        }
        return super.getPiece(x, y);
    }
}
