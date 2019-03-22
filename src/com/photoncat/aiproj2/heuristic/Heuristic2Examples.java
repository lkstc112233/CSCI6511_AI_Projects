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
                        "-X---O------\n" +
                        "--O-OO------\n" +
                        "---O-OXOO---\n" +
                        "-XOOOOOX----\n" +
                        "-----OXXX---\n" +
                        "-----XOXXX--\n" +
                        "----X-XXX---\n" +
                        "--------X--O\n" +
                        "--O-----O---\n" +
                        "------------\n",6);
        boardToAdd.setLastMove(new Move(3, 4), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        boardToAdd = new LoadedBoard(
                        "------------\n" +
                        "-----X------\n" +
                        "-X---O------\n" +
                        "--O--O------\n" +
                        "---O-OXOO---\n" +
                        "-XOOOOOX----\n" +
                        "-----OXXX---\n" +
                        "-----XOXXX--\n" +
                        "----XOXXX---\n" +
                        "--------X--O\n" +
                        "--O-----O---\n" +
                        "------------\n",6);
        boardToAdd.setLastMove(new Move(8, 5), Board.PieceType.CIRCLE);
        boards.add(boardToAdd);
        for (Board board: boards) {
            System.out.println(board.toString());
            System.out.println("Gameover: " + board.gameover());
            System.out.println("Wins: " + board.wins());
            System.out.println("Heuristic: " + heuristics.heuristic(board));
        }
    }
}
