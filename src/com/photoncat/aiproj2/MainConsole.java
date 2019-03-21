package com.photoncat.aiproj2;

import com.photoncat.aiproj2.game.Game;
import com.photoncat.aiproj2.heuristic.Heuristic2;
import com.photoncat.aiproj2.io.Adapter;
import com.photoncat.aiproj2.io.ConsoleAdapter;
import com.photoncat.aiproj2.io.SelfAdapter;

public class MainConsole {
    public static void main(String[] args) {
        Adapter adapter = new SelfAdapter();
        Game game = new Game(adapter, 0, 10, 5, new Heuristic2());
        game.start();
        Game game2 = new Game(adapter, -game.getGameId(), new Heuristic2());
        game2.start();
    }
}
