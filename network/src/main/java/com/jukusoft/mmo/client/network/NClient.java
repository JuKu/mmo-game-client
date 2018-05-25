package com.jukusoft.mmo.client.network;

import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.login.LoginManager;
import com.jukusoft.mmo.client.network.utils.MessageUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class NClient {

    protected WritableGame game;

    //network variables
    protected VertxOptions vertxOptions = new VertxOptions();
    protected Vertx vertx = null;
    protected NetClientOptions options = new NetClientOptions();
    protected NetClient client = null;

    //socket
    protected NetSocket socket = null;

    protected int rttInterval = 100;
    protected AtomicBoolean rttMsgReceived = new AtomicBoolean(true);
    protected AtomicLong lastRttTime = new AtomicLong(0);

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

        //get rtt interval
        this.rttInterval = Integer.parseInt(cSection.getOrDefault("rttInterval", "500"));
    }

    /**
    * start network module, connect to proxy server
    */
    public void start () {
        this.vertx = Vertx.vertx(vertxOptions);

        this.client = vertx.createNetClient(options);

        //register connection executor
        ServerManager.getInstance().setConnectionExecutor((ServerManager.ConnectRequest req) -> {
            //try to connect
            client.connect(req.server.port, req.server.ip, res -> this.connect(req, res));
        });

        //register login executor
        LoginManager.getInstance().setLoginExecutor((LoginManager.LoginRequest req) -> {
            LocalLogger.print("try to login user");

            req.loginHandler.handle(LoginManager.LOGIN_RESPONSE.WRONG_CREDENTIALS);
        });
    }

    protected void connect (ServerManager.ConnectRequest req, AsyncResult<NetSocket> res) {
        if (res.succeeded()) {
            //get socket
            socket = res.result();

            LocalLogger.print("Connected to proxy server " + req.server.ip + ":" + req.server.port);

            this.setRttTimer();

            //call handler, so UI can be updated
            req.handler.handle(true);
        } else {
            LocalLogger.warn("Failed to connect: " + res.cause().getMessage());

            //call handler, so UI can be updated
            req.handler.handle(false);
        }
    }

    protected void setRttTimer () {
        this.vertx.setPeriodic(this.rttInterval, timerID -> executeRTTCheck());
    }

    /**
    * determine round-trip-time
    */
    protected void executeRTTCheck () {
        //check, if a rtt message was already sended and no response received
        if (!this.rttMsgReceived.get()) {
            return;
        }

        //set current timestamp
        lastRttTime.set(System.currentTimeMillis());

        //send rtt message
        Buffer msg = MessageUtils.createRTTMsg();
        this.send(msg);
    }

    public void send (Buffer content) {
        if (this.socket == null) {
            throw new IllegalStateException("no connection is established.");
        }

        this.socket.write(content);
    }

    /**
     * disconnect server
     */
    public void stop () {
        this.client.close();
        this.vertx.close();
    }

}
