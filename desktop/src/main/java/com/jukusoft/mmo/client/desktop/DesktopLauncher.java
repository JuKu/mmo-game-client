package com.jukusoft.mmo.client.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.jukusoft.mmo.client.desktop.config.WindowConfig;
import com.jukusoft.mmo.client.desktop.impl.GameImpl;
import com.jukusoft.mmo.client.engine.cache.Cache;
import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.time.GameTime;
import com.jukusoft.mmo.client.engine.utils.Utils;
import com.jukusoft.mmo.client.engine.version.Version;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.gui.GameGUI;
import com.jukusoft.mmo.client.gui.assetmanager.GameAssetManager;
import com.jukusoft.mmo.client.network.NClient;
import com.jukusoft.mmo.client.network.Protocol;
import com.jukusoft.mmo.client.network.handler.impl.AuthHandler;
import com.jukusoft.mmo.client.network.handler.impl.GeneralClientStateHandler;

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

        Utils.printSection("Version Information");
        printStartUpInfo(DesktopLauncher.class);
        Version.setInstance(new Version(DesktopLauncher.class));

        //start game
        WritableGame game = new GameImpl();

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

        if (!new File(Cache.getInstance().getPath() + "assets/loading/loading.pack.atlas").exists()) {
            LocalLogger.print("pack loading assets...");
            TexturePacker.process(new TexturePacker.Settings(), "./data/loading", Cache.getInstance().getPath() + "assets/loading", "loading.pack");
        } else {
            LocalLogger.print("textures are already packed.");
        }

        Utils.printSection("Asset Manager");
        LocalLogger.print("initialize asset manager...");
        GameAssetManager.getInstance();

        Utils.printSection("Init game");

        //initialize game time
        GameTime.getInstance();

        //init game
        game.init();

        Utils.printSection("Networking");

        //start networking
        NClient nClient = new NClient(game);
        nClient.loadConfig(new File("./config/network.cfg"));
        nClient.start();

        //add handlers
        nClient.addHandler(Protocol.MSG_TYPE_AUTH, new AuthHandler(nClient, game));
        nClient.addHandler(Protocol.MSG_TYPE_GENERAL_CLIENT_STATE_INFORMATION, new GeneralClientStateHandler());

        Utils.printSection("Window");

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        //load window config
        WindowConfig windowConfig = new WindowConfig("./config/window.cfg");
        windowConfig.fillConfig(config);

        // start game
        new Lwjgl3Application(new GameGUI(game), config);

        //close game
        LocalLogger.print("close application");
        System.exit(0);
    }

    public static void printStartUpInfo (Class<?> cls) {
        //load version
        Version version = new Version(cls);

        LocalLogger.print("/***************************************************************");
        LocalLogger.print("*");
        LocalLogger.print("*  MMO Game Client");
        LocalLogger.print("*  ----------------");
        LocalLogger.print("*");
        LocalLogger.print("*  Version: " + version.getVersion());
        LocalLogger.print("*  Build: " + version.getRevision());
        LocalLogger.print("*");
        LocalLogger.print("*  Build JDK: " + version.getBuildJdk());
        LocalLogger.print("*  Build time: " + version.getBuildTime());
        LocalLogger.print("*  Vendor ID: " + (!version.getVendor().equals("n/a") ? version.getVendor() : version.getVendorID()));
        LocalLogger.print("*");
        LocalLogger.print("***************************************************************/");
    }

}
