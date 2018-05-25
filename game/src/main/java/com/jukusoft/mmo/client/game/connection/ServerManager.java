package com.jukusoft.mmo.client.game.connection;

import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.utils.FileUtils;
import com.jukusoft.mmo.client.engine.utils.SocketUtils;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    protected static final ServerManager instance = new ServerManager();

    protected List<Server> list = new ArrayList<>();

    protected Server selectedServer = null;
    protected Handler<ConnectRequest> connectionExecutor = null;

    public ServerManager () {
        //
    }

    public void loadFromConfig (File config) throws IOException {
        //check, if config file exists
        if (!config.exists()) {
            throw new IllegalStateException("server config file doesnt exists: " + config.getAbsolutePath());
        }

        list.clear();

        String content = FileUtils.readFile(config.getAbsolutePath(), StandardCharsets.UTF_8);
        JsonObject json = new JsonObject(content);

        JsonArray servers = json.getJsonArray("servers");

        for (int i = 0; i < servers.size(); i++) {
            JsonObject server = servers.getJsonObject(i);

            String ip = server.getString("ip");
            int port = server.getInteger("port");
            String title = server.getString("title");
            String description = server.getString("description");

            //check, if server is online
            boolean online = SocketUtils.checkRemoteTCPPort(ip, port, 500);

            Server obj = new Server(ip, port, title, description, online);
            list.add(obj);
        }
    }

    public static ServerManager getInstance() {
        return instance;
    }

    public List<Server> listServers () {
        return this.list;
    }

    public Server getSelectedServer () {
        if (this.selectedServer == null) {
            throw new IllegalStateException("no server was selected before.");
        }

        return this.selectedServer;
    }

    public void setSelectServer (Server server) {
        this.selectedServer = server;
    }

    /**
    * try to connect to server
    */
    public void connect (Handler<Boolean> connectHandler) {
        if (this.connectionExecutor == null) {
            throw new IllegalStateException("ServerManager: no connection executor is registered.");
        }

        if (this.selectedServer == null) {
            throw new IllegalStateException("no server was selected before. Call setSelectedServer() first.");
        }

        this.connectionExecutor.handle(new ConnectRequest(this.selectedServer, connectHandler));
    }

    public void setConnectionExecutor(Handler<ConnectRequest> connectionExecutor) {
        this.connectionExecutor = connectionExecutor;
    }

    protected static Server createServer (String ip, int port, String title, String description, boolean online) {
        return new Server(ip, port, title, description, online);
    }

    public static class Server {

        public final String ip;
        public final int port;
        public final String title;
        public final String description;
        public final boolean online;

        public Server (String ip, int port, String title, String description, boolean online) {
            this.ip = ip;
            this.port = port;
            this.title = title;
            this.description = description;
            this.online = online;
        }

    }

    public static class ConnectRequest {
        public final Server server;
        public final Handler<Boolean> handler;

        public ConnectRequest (Server server, Handler<Boolean> handler) {
            this.server = server;
            this.handler = handler;
        }
    }

}
