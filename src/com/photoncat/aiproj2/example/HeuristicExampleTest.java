package com.photoncat.aiproj2.example;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Heuristics;
import com.photoncat.aiproj2.io.LoadedBoard;

/**
 * The test class for {@link HeuristicExample}. You should always write an unit test for the program.
 */
public class HeuristicExampleTest {
    public static void main(String[] args) {
        Board board = new LoadedBoard("---\n-O-\n-X-\n", 3);
        Heuristics example = new HeuristicExample();
        if (example.heuristic(board) != 0) throw new AssertionError();
        board = new LoadedBoard("---\n-O-\nOX-\n", 3);
        if (example.heuristic(board) != 2) throw new AssertionError();
        board = new LoadedBoard("---\n-O-\nOXX\n", 3);
        if (example.heuristic(board) != 0) throw new AssertionError();
        System.out.println("All test passed");
    }
}
