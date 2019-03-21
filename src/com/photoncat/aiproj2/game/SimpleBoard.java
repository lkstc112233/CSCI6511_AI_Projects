package com.photoncat.aiproj2.game;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.interfaces.MutableBoard;

import java.util.Stack;

public class SimpleBoard implements MutableBoard {
    protected PieceType[][] board;
    private int m;
    private int maximumSteps;
    protected int steps = 0;
    private PieceType winner = null;
    protected PieceType next = PieceType.CIRCLE;
    private Stack<Move> previousSteps = new Stack<>();
    public SimpleBoard(int size, int m) {
        board = new PieceType[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                board[i][j] = PieceType.NONE;
            }
        }
        this.m = m;
        this.maximumSteps = size * size;
    }

    public SimpleBoard(Board boardInput, Move lastMove) {
        int size = boardInput.getSize();
        board = new PieceType[size][size];
        // This is a must since we are inheriting the base class.
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                board[i][j] = boardInput.getPiece(i, j);
                if (board[i][j] != PieceType.NONE) {
                    steps += 1;
                }
            }
        }
        m = boardInput.getM();
        maximumSteps = boardInput.getSize() * boardInput.getSize();
        // Redo last move
        if (lastMove != null) {
            this.steps -= 1;
            next = board[lastMove.x][lastMove.y];
            board[lastMove.x][lastMove.y] = PieceType.NONE;
            winner = null;
            putPiece(lastMove);
        }
    }

    @Override
    public int getSize() {
        return board.length;
    }

    @Override
    public int getM() {
        return m;
    }

    @Override
    public PieceType getPiece(int x, int y) {
        int size = getSize();
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return null;
        }
        return board[x][y];
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
        int x = move.x;
        int y = move.y;
        if (gameover() || getPiece(x, y) != PieceType.NONE) {
            return false;
        }
        board[x][y] = next;
        // Check win condition.
        if (WinningChecker.winningCheck(this, x, y, next)) {
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
        int x = step.x;
        int y = step.y;
        steps -= 1;
        board[x][y] = PieceType.NONE;
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
