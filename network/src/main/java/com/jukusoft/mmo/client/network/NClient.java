package com.jukusoft.mmo.client.network;

import com.jukusoft.mmo.client.engine.utils.FileUtils;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NClient {

    protected WritableGame game;

    //network variables
    protected VertxOptions vertxOptions = new VertxOptions();
    protected Vertx vertx = null;
    protected NetClientOptions options = new NetClientOptions();
    protected NetClient client = null;

    /**
    * default constructor
     *
     * @param game game instance
    */
    public NClient (WritableGame game) {
        //
    }

    public void loadConfig (File configFile) throws IOException {
        if (configFile == null) {
            throw new NullPointerException("config file cannot be null.");
        }

        if (!configFile.exists()) {
            throw new FileNotFoundException("config file doesnt exists: " + configFile.getAbsolutePath());
        }

        Ini ini = new Ini(configFile);
        Profile.Section section = ini.get("Network");

        //get number of threads
        int eventThreads = Integer.parseInt(section.getOrDefault("eventLoopPoolSize", "2"));
        int workerThreads = Integer.parseInt(section.getOrDefault("workerPoolSize", "2"));

        //set thread count
        vertxOptions.setEventLoopPoolSize(eventThreads);
        vertxOptions.setWorkerPoolSize(workerThreads);

        //set NetClient options
        Profile.Section cSection = ini.get("Client");

        int timeout = Integer.parseInt(cSection.getOrDefault("timeout", "1000"));
        options.setConnectTimeout(timeout);

        int reconnectAttempts = Integer.parseInt(cSection.getOrDefault("reconnectAttempts", "10"));
        int reconnectInterval = Integer.parseInt(cSection.getOrDefault("reconnectInterval", "500"));
        options.setReconnectAttempts(reconnectAttempts);
        options.setReconnectInterval(reconnectInterval);

        boolean logging = Boolean.parseBoolean(cSection.getOrDefault("logging", "false"));
        options.setLogActivity(logging);
    }

    /**
    * start network module, connect to proxy server
    */
    public void start () {
        this.vertx = Vertx.vertx(vertxOptions);

        this.client = vertx.createNetClient(options);

        //register connection executor
        ServerManager.getInstance().setConnectionExecutor((ServerManager.ConnectRequest req) -> {
            //TODO: try to connect
            req.handler.handle(false);


        });
    }

    /**
     * disconnect server
     */
    public void stop () {
        //
    }

}
