package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * A board intended to be used as a draft. Wraps a board.
 */
class HashDraftBoard implements MutableBoard {
    private Board board;
    private PieceType next;
    private int maximumSteps;
    private int steps = 0;
    private Map<Move, PieceType> moves = new HashMap<>();
    private PieceType winner = null;
    private Stack<Move> previousSteps = new Stack<>();

    public HashDraftBoard(Board board, PieceType nextType) {
        this.board = board;
        this.next = nextType;
        maximumSteps = board.getSize() * board.getSize();
        for (int x = 0; x < board.getSize(); ++x) {
            for (int y = 0; y < board.getSize(); ++y) {
                if (board.getPiece(x, y) != PieceType.NONE) {
                    steps += 1;
                }
            }
        }
    }

    @Override
    public int getSize() {
        return board.getSize();
    }

    @Override
    public int getM() {
        return board.getM();
    }

    @Override
    public PieceType getPiece(int x, int y) {
        PieceType type = board.getPiece(x, y);
        if (type != PieceType.NONE) {
            return type;
        }
        Move move = new Move(x, y);
        if (moves.containsKey(move)) {
            return moves.get(move);
        }
        return PieceType.NONE;
    }

    @Override
    public PieceType wins() {
        if (winner == null) {
            return PieceType.NONE;
        }
        return winner;
    }

    @Override
    public boolean gameover() {
        return winner != null;
    }

    @Override
    public boolean putPiece(Move move) {
        if (board.getPiece(move.x, move.y) != PieceType.NONE) {
            return false;
        }
        if (moves.containsKey(move)) {
            return false;
        }
        moves.put(move, next);
        // Check win condition.
        if (WinningChecker.winningCheck(this, move.x, move.y, next)) {
            winner = next;
        }
        steps += 1;
        previousSteps.add(move);
        if (steps >= maximumSteps && winner == null) {
            winner = PieceType.NONE;
        }
        toggleNext();
        return true;
    }

    @Override
    public void takeBack() {
        // Does nothing when take back un-happened step.
        if (previousSteps.empty()) {
            return;
        }
        toggleNext();
        Move step = previousSteps.pop();
        steps -= 1;
        moves.remove(step);
        winner = null;
    }

    private void toggleNext() {
        next = next == PieceType.CIRCLE ? PieceType.CROSS : PieceType.CIRCLE;
    }

    @Override
    public String toString() {
        return Board.toString(this);
    }
}