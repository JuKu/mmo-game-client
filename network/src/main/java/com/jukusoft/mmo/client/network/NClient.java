package com.jukusoft.mmo.client.network;

import com.jukusoft.mmo.client.engine.utils.FileUtils;
import com.jukusoft.mmo.client.game.WritableGame;
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
    }

    /**
    * start network module, connect to proxy server
    */
    public void start () {
        this.vertx = Vertx.vertx(vertxOptions);

        NetClientOptions options = new NetClientOptions().setConnectTimeout(10000);
        this.client = vertx.createNetClient(options);
    }

    /**
     * disconnect server
     */
    public void stop () {
        //
    }

}
