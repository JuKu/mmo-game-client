package com.jukusoft.mmo.client.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.jukusoft.mmo.client.desktop.config.WindowConfig;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.gui.GameGUI;
import com.jukusoft.mmo.client.network.NClient;

public class DesktopLauncher {

    public static void main (String[] args) {
        //start game
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    protected static void start () throws Exception {
        //start game
        WritableGame game = null;

        //TODO: init game

        //TODO: start networking
        NClient nClient = new NClient(game);
        nClient.start();

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        //load window config
        WindowConfig windowConfig = new WindowConfig("./config/window.cfg");
        windowConfig.fillConfig(config);

        // start game
        new Lwjgl3Application(new GameGUI(game), config);
    }

}
