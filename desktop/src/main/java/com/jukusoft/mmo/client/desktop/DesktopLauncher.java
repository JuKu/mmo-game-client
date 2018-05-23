package com.jukusoft.mmo.client.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.jukusoft.mmo.client.desktop.config.WindowConfig;
import com.jukusoft.mmo.client.engine.cache.Cache;
import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.time.GameTime;
import com.jukusoft.mmo.client.engine.utils.Utils;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.gui.GameGUI;
import com.jukusoft.mmo.client.network.NClient;

import java.io.File;

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
        Utils.printSection("Game Start");

        //start game
        WritableGame game = null;

        Utils.printSection("Servers");
        LocalLogger.print("load servers");

        //load servers
        ServerManager.getInstance().loadFromConfig(new File("./config/servers.json"));
        LocalLogger.print(ServerManager.getInstance().listServers().size() + " servers found.");

        for (ServerManager.Server server : ServerManager.getInstance().listServers()) {
            LocalLogger.print(" - " + server.title + " (" + server.ip + ":" + server.port + " - " + (server.online ? "online" : "offline") + ")");
        }

        Utils.printSection("Cache");
        LocalLogger.print("Initialize cache...");
        Cache.init(new File("./config/cache.cfg"));
        LocalLogger.print("cache directory: " + Cache.getInstance().getPath());

        Utils.printSection("Texture Packer");
        LocalLogger.print("pack loading assets...");
        TexturePacker.process(new TexturePacker.Settings(), "./data/loading", Cache.getInstance().getPath() + "assets/loading", "loading.pack");

        Utils.printSection("Init game");

        //initialize game time
        GameTime.getInstance();

        //TODO: init game

        Utils.printSection("Networking");

        //TODO: start networking
        NClient nClient = new NClient(game);
        nClient.start();

        Utils.printSection("Window");

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        //load window config
        WindowConfig windowConfig = new WindowConfig("./config/window.cfg");
        windowConfig.fillConfig(config);

        // start game
        new Lwjgl3Application(new GameGUI(game), config);
    }

}
