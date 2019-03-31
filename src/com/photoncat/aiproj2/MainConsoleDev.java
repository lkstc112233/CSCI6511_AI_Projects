package com.photoncat.aiproj2;

import com.photoncat.aiproj2.game.Game;
import com.photoncat.aiproj2.heuristic.Heuristic2;
import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.io.Adapter;
import com.photoncat.aiproj2.io.ConsoleAdapter;
import com.photoncat.aiproj2.io.NetworkAdapter;
import com.photoncat.aiproj2.io.SelfAdapter;

import java.io.File;
import java.util.Scanner;

public class MainConsoleDev {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please select what you want:");
            System.out.println("1. Play against self");
            System.out.println("2. Play against another team");
            System.out.println("3. Play against human");
            System.out.println("other. exit");
            System.out.print("> ");
            int selection = scanner.nextInt();
            int teamId = 0;
            int boardSize = 12;
            int target = 6;
            if (selection != 1 && selection != 2 && selection != 3) {
                System.out.println("Thank you for using!");
                System.exit(0);
            }
            int pick = 0;
            if (selection == 2 || selection == 3) {
                System.out.println("Please select what you want:");
                System.out.println("1. Play First");
                System.out.println("2. Play Second");
                System.out.println("other. exit");
                System.out.print("> ");
                pick = scanner.nextInt();
                if (pick != 1 && pick != 2) {
                    System.out.println("Thank you for using!");
                    System.exit(0);
                }
            }
            if (selection == 2) {
                System.out.println("Please Enter Id: ");
                teamId = scanner.nextInt();
            }
            System.out.println("Please Enter BoardSize: ");
            boardSize = scanner.nextInt();
            System.out.println("Please Enter target: ");
            target = scanner.nextInt();
            switch (selection) {
                case 1: {
                    Adapter selfAdapter = new SelfAdapter();
                    Game game = new Game(selfAdapter, 0, boardSize, target, new Heuristic2());
                    game.start();
                    Game game2 = new Game(selfAdapter, -game.getGameId(), new Heuristic2());
                    game2.start();
                    game.join();
                    game2.join();
                    System.out.println("Game over.");
                    switch (selfAdapter.getBoard(game.getGameId()).wins()) {
                        case CROSS:
                            System.out.println("Cross wins.");
                            break;
                        case CIRCLE:
                            System.out.println("Circle wins.");
                            break;
                        case NONE:
                            System.out.println("Game draws.");
                            break;
                    }
                }
                    break;
                case 2: {
                    Adapter netAdapter = new NetworkAdapter(new File(args[1]));
                    Game game;
                    if (pick == 1) {
                        game = new Game(netAdapter, teamId, boardSize, target, new Heuristic2());
                    } else {
                        game = new Game(netAdapter, teamId, new Heuristic2());
                    }
                    game.start();
                    game.join();
                    System.out.println("Game over.");
                    switch (netAdapter.getBoard(game.getGameId()).wins()) {
                        case CROSS:
                            System.out.println("Cross wins.");
                            break;
                        case CIRCLE:
                            System.out.println("Circle wins.");
                            break;
                        case NONE:
                            System.out.println("Game draws.");
                            break;
                    }
                }
                    break;
                case 3:{
                    Adapter consoleAdapter = new ConsoleAdapter();
                    Game game = new Game(consoleAdapter, teamId, boardSize, target, new Heuristic2());
                    if (pick == 2) {
                        game = new Game(consoleAdapter, teamId, new Heuristic2());
                    }
                    game.start();
                    game.join();
                    System.out.println("Game over.");
                    Board.PieceType winner = consoleAdapter.getBoard(game.getGameId()).wins();
                    if (pick == 2) {
                        winner = winner.flipPiece();
                    }
                    switch (winner) {
                        case CROSS:
                            System.out.println("Cross wins.");
                            break;
                        case CIRCLE:
                            System.out.println("Circle wins.");
                            break;
                        case NONE:
                            System.out.println("Game draws.");
                            break;
                    }
                }
                    break;
            }
        }
    }
}
