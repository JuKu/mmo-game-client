package com.jukusoft.mmo.client.network;

import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.utils.ByteUtils;
import com.jukusoft.mmo.client.engine.utils.EncryptionUtils;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.login.LoginManager;
import com.jukusoft.mmo.client.network.handler.NetHandler;
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
import java.security.PublicKey;
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
    protected int sendDelay = 0;
    protected int receiveDelay = 0;

    //array with handlers (8 bit --> 256 possible types)
    protected NetHandler[] handlerArray = new NetHandler[256];

    /**
    * default constructor
     *
     * @param game game instance
    */
    public NClient (WritableGame game) {
        this.game = game;

        //initialize array
        for (int i = 0; i < handlerArray.length; i++) {
            handlerArray[i] = null;
        }
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

        //get delay
        this.sendDelay = Integer.parseInt(cSection.getOrDefault("sendDelay", "0"));
        this.receiveDelay = Integer.parseInt(cSection.getOrDefault("receiveDelay", "0"));
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

            //initialize socket
            this.initSocket(socket);

            LocalLogger.print("Connected to proxy server " + req.server.ip + ":" + req.server.port);

            this.setRttTimer();

            LocalLogger.print("request rsa public key...");

            //request public key
            Buffer msg = MessageUtils.createPublicKeyRequest();
            this.send(msg);

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

        this.rttMsgReceived.set(false);

        //set current timestamp
        lastRttTime.set(System.currentTimeMillis());

        //send rtt message
        Buffer msg = MessageUtils.createRTTMsg();
        this.send(msg);
    }

    protected void initSocket (NetSocket socket) {
        //set handler
        socket.handler(this::handleMessageWithDelay);
        socket.closeHandler(this::onConnectionClosed);
    }

    protected void handleMessageWithDelay (Buffer content) {
        if (this.receiveDelay > 0) {
            //delay message and handle them
            vertx.setTimer(this.receiveDelay, timerID -> handleMessage(content));
        } else {
            //handle message without delay
            handleMessage(content);
        }
    }

    protected void handleMessage (Buffer content) {
        if (content == null) {
            throw new NullPointerException("buffer cannot be null.");
        }

        if (content.length() < Protocol.MSG_HEADER_LENGTH) {
            throw new IllegalArgumentException("buffer doesnt contains full header.");
        }

        byte type = content.getByte(0);
        byte extendedType = content.getByte(1);

        //check, if message is RTT message
        if (type == Protocol.MSG_TYPE_PROXY) {
            if (extendedType == Protocol.MSG_EXTENDED_TYPE_RTT) {
                //get current timestamp
                long now = System.currentTimeMillis();

                //calculate rtt & ping
                long rtt = now - this.lastRttTime.get();
                long ping = rtt / 2;

                //set ping
                game.setPing((int) ping);

                //reset flag
                this.rttMsgReceived.set(true);

                return;
            } else if (extendedType == Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_RESPONSE) {
                //get length of public key
                int length = content.getInt(Protocol.MSG_BODY_OFFSET);

                //read bytes
                byte[] key = content.getBytes(Protocol.MSG_BODY_OFFSET + 4, Protocol.MSG_BODY_OFFSET + 4 + length);

                if (key.length != length) {
                    throw new IllegalStateException("received rsa key doenst have expected lenght.");
                }

                //generate public key from byte array
                try {
                    PublicKey publicKey = EncryptionUtils.getPubKeyFromArray(key);

                    //initialize EncryptionUtils
                    EncryptionUtils.init(publicKey);

                    LocalLogger.print("RSA public key received.");
                } catch (Exception e) {
                    LocalLogger.warn("Couldnt create public key from byte array.");
                    LocalLogger.printStacktrace(e);
                }

                return;
            }
        }

        int typeInt = ByteUtils.byteToUnsignedInt(type);

        //check, if handler is specified
        if (this.handlerArray[typeInt] != null) {
            //call handler
            NetHandler handler = this.handlerArray[typeInt];

            //execute handler
            handler.handle(content, this, this.game);
        } else {
            LocalLogger.warn("No handler is specified for type 0x" + ByteUtils.byteToHex(type) + ".");
        }
    }

    public void addHandler (byte type, NetHandler handler) {
        //convert type to unsigned int value
        int typeInt = ByteUtils.byteToUnsignedInt(type);

        if (handler == null) {
            throw new NullPointerException("handler is null.");
        }

        //check, if handler is already registered
        if (this.handlerArray[typeInt] != null) {
            throw new IllegalStateException("handler for type 0x" + ByteUtils.byteToHex(type) + " is already registered");
        }

        this.handlerArray[typeInt] = handler;
    }

    protected void onConnectionClosed(Void v) {
        LocalLogger.warn("Connection was closed.");

        //TODO: inform GUI, so reconnect screen can be shown
    }

    public void send (Buffer content) {
        if (this.socket == null) {
            throw new IllegalStateException("no connection is established.");
        }

        //if configuration has send delay enable, delay sending message to simulate external server
        if (this.sendDelay > 0) {
            vertx.setTimer(this.sendDelay, timerID -> this.socket.write(content));
            return;
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
