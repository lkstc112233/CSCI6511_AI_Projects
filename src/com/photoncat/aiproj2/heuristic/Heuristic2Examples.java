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
                "------------\n" +
                        "-----X------\n" +
                        "-----O------\n" +
                        "-----O------\n" +
                        "-----OX-O---\n" +
                        "-XOOOOOX----\n" +
                        "-----OX-X---\n" +
                        "-----XOXXX--\n" +
                        "----X---X---\n" +
                        "--------X--O\n" +
                        "--O-----O---\n" +
                        "------------\n",6);
        boardToAdd.setLastMove(new Move(10, 8), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        boardToAdd = new LoadedBoard(
                "------------\n" +
                        "-----X------\n" +
                        "-----O------\n" +
                        "-----O------\n" +
                        "---O-OX-O---\n" +
                        "-XOOOOOX----\n" +
                        "-----OX-X---\n" +
                        "-----XOXXX--\n" +
                        "----X---X---\n" +
                        "--------X--O\n" +
                        "--O---------\n" +
                        "------------\n",6);
        boardToAdd.setLastMove(new Move(4, 3), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        boardToAdd = new LoadedBoard(
                "------------\n" +
                        "-----X------\n" +
                        "-----O------\n" +
                        "-----O------\n" +
                        "-----OX-O---\n" +
                        "-XOOOOOXO---\n" +
                        "-----OX-X---\n" +
                        "-----XOXXX--\n" +
                        "----X---X---\n" +
                        "--------X--O\n" +
                        "--O---------\n" +
                        "------------\n",6);
        boardToAdd.setLastMove(new Move(5, 8), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        for (Board board: boards) {
            System.out.println(board.toString());
            System.out.println("Gameover: " + board.gameover());
            System.out.println("Wins: " + board.wins());
            System.out.println("Heuristic: " + heuristics.heuristic(board));
        }
    }
}
