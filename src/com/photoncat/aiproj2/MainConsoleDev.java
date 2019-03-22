package com.photoncat.aiproj2;

import com.photoncat.aiproj2.game.Game;
import com.photoncat.aiproj2.heuristic.Heuristic2;
import com.photoncat.aiproj2.io.Adapter;
import com.photoncat.aiproj2.io.ConsoleAdapter;
import com.photoncat.aiproj2.io.SelfAdapter;

public class MainConsoleDev {
    public static void main(String[] args) {
        Adapter adapter = new ConsoleAdapter();
        Game game = new Game(adapter, 0, 12, 6, new Heuristic2());
        game.start();
//        Game game2 = new Game(adapter, -game.getGameId(), new Heuristic2());
//        game2.start();
    }
}
