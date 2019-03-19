package com.photoncat.aiproj2.heuristic;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Heuristics;
import com.photoncat.aiproj2.interfaces.Move;
import com.photoncat.aiproj2.io.LoadedBoard;

public class Heuristic2Examples {
    public static void main(String[] args) {
        Heuristics heuristics = new Heuristic2();
        LoadedBoard board1 = new LoadedBoard("XX-X\n-OO-\n-OO-\n----",3); // Actually took decision.
        board1.setLastMove(new Move(2, 2), Board.PieceType.CIRCLE);
        LoadedBoard board2 = new LoadedBoard("XXOX\n-OO-\n-O--\n----",3); // Obviously better decision.
        board2.setLastMove(new Move(0, 2), Board.PieceType.CIRCLE);
        LoadedBoard board3 = new LoadedBoard("XX-X\n-OO-\n-O--\nO---",3); // Obviously best decision.
        board3.setLastMove(new Move(3, 0), Board.PieceType.CIRCLE);
        System.out.println("For board 1:");
        System.out.println(board1.toString());
        System.out.println("Gameover: " + board1.gameover());
        System.out.println("Wins: " + board1.wins());
        System.out.println("Heuristic: " + heuristics.heuristic(board1));
        System.out.println("For board 2:");
        System.out.println(board2.toString());
        System.out.println("Gameover: " + board2.gameover());
        System.out.println("Wins: " + board2.wins());
        System.out.println("Heuristic: " + heuristics.heuristic(board2));
        System.out.println("For board 3:");
        System.out.println(board3.toString());
        System.out.println("Gameover: " + board3.gameover());
        System.out.println("Wins: " + board3.wins());
        System.out.println("Heuristic: " + heuristics.heuristic(board3));
    }
}
