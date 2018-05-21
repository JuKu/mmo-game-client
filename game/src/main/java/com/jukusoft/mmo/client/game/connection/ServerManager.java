package com.jukusoft.mmo.client.game.connection;

import com.jukusoft.mmo.client.game.utils.FileUtils;
import com.sun.org.apache.xml.internal.security.Init;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    protected static final ServerManager instance = new ServerManager();

    protected List<Server> list = new ArrayList<>();

    public ServerManager () {
        //
    }

    public void loadFromConfig (File config) throws IOException {
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

            Server obj = new Server(ip, port, title, description);
            list.add(obj);
        }
    }

    public static ServerManager getInstance() {
        return instance;
    }

    public List<Server> listServers () {
        return this.list;
    }

    public class Server {

        public final String ip;
        public final int port;
        public final String title;
        public final String description;

        public Server (String ip, int port, String title, String description) {
            this.ip = ip;
            this.port = port;
            this.title = title;
            this.description = description;
        }

    }

}
