package com.photoncat.aiproj2.heuristic;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Heuristics;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.io.LoadedBoard;

import java.util.ArrayList;
import java.util.List;

public class Heuristic2Examples {
    public static void main(String[] args) {
        Heuristics heuristics = new Heuristic2();
        List<Board> boards = new ArrayList<>();
        LoadedBoard boardToAdd = new LoadedBoard(
                "----------\n" +
                "----------\n" +
                "----------\n" +
                "----X-----\n" +
                "----OX----\n" +
                "----O-----\n" +
                "----------\n" +
                "----O-----\n" +
                "----------\n" +
                "----------\n",5);
        boardToAdd.setLastMove(new Move(7, 4), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        boardToAdd = new LoadedBoard("----------\n" +
                "----------\n" +
                "----------\n" +
                "----X-----\n" +
                "----OX----\n" +
                "----O-O---\n" +
                "----------\n" +
                "----------\n" +
                "----------\n" +
                "----------\n",5);
        boardToAdd.setLastMove(new Move(5, 6), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        boardToAdd = new LoadedBoard("----------\n" +
                "----------\n" +
                "----------\n" +
                "----X-----\n" +
                "----OX----\n" +
                "---OO-----\n" +
                "----------\n" +
                "----------\n" +
                "----------\n" +
                "----------\n",5);
        boardToAdd.setLastMove(new Move(5, 3), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        boardToAdd = new LoadedBoard(
                "----------\n" +
                        "----------\n" +
                        "----------\n" +
                        "----X-----\n" +
                        "----OX----\n" +
                        "----O-----\n" +
                        "----O-----\n" +
                        "----------\n" +
                        "----------\n" +
                        "----------\n",5);
        boardToAdd.setLastMove(new Move(6, 4), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        boardToAdd = new LoadedBoard(
                "----------\n" +
                        "----------\n" +
                        "----------\n" +
                        "----X-----\n" +
                        "----OX----\n" +
                        "----O-X---\n" +
                        "----------\n" +
                        "----O-----\n" +
                        "----O-----\n" +
                        "----------\n",5);
        boardToAdd.setLastMove(new Move(8, 4), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        for (Board board: boards) {
            System.out.println(board.toString());
            System.out.println("Gameover: " + board.gameover());
            System.out.println("Wins: " + board.wins());
            System.out.println("Heuristic: " + heuristics.heuristic(board));
        }
    }
}
