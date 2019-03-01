package com.photoncat.aiproj2;

import com.photoncat.aiproj2.game.Game;
import com.photoncat.aiproj2.io.Adapter;
import com.photoncat.aiproj2.io.ConsoleAdapter;

public class MainConsole {
    public static void main(String[] args) {
        Adapter adapter = new ConsoleAdapter();
        Game game = new Game(adapter, 0, 4, 3);
        game.start();
    }
}
