package com.jukusoft.mmo.client.network;

import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.login.LoginManager;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NClientTest {

    @Test
    public void testConstructor () {
        WritableGame game = Mockito.mock(WritableGame.class);

        new NClient(game);
    }

    @Test (expected = NullPointerException.class)
    public void testLoadNullConfig () throws IOException {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.loadConfig(null);
    }

    @Test (expected = FileNotFoundException.class)
    public void testLoadNotExistentFileConfig () throws IOException {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.loadConfig(new File("not-existent-file.cfg"));
    }

    @Test
    public void testLoadConfig () throws IOException {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.loadConfig(new File("../config/network.cfg"));
    }

    @Test
    public void testConnect () {
        WritableGame game = Mockito.mock(WritableGame.class);

        ServerManager.ConnectRequest req = new ServerManager.ConnectRequest(Mockito.mock(ServerManager.Server.class), Mockito.mock(Handler.class));

        NClient client = new NClient(game);
        client.connect(req, Future.failedFuture("test"));
    }

    @Test
    public void testConnect1 () {
        WritableGame game = Mockito.mock(WritableGame.class);

        ServerManager.ConnectRequest req = new ServerManager.ConnectRequest(Mockito.mock(ServerManager.Server.class), Mockito.mock(Handler.class));

        NClient client = new NClient(game);
        client.start();
        client.connect(req, Future.succeededFuture(null));
        client.stop();
    }

    @Test
    public void testConnect2 () {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.start();
        ServerManager.getInstance().setSelectServer(new ServerManager.Server("127.0.0.1", 10, "test", "test", true));
        ServerManager.getInstance().connect(Mockito.mock(Handler.class));
        client.stop();

        ServerManager.getInstance().setSelectServer(null);
        ServerManager.getInstance().setConnectionExecutor(null);
    }

    @Test
    public void testLogin () {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.start();
        ServerManager.getInstance().setSelectServer(new ServerManager.Server("127.0.0.1", 10, "test", "test", true));

        LoginManager.getInstance().login("user", "password", Mockito.mock(Handler.class));

        client.stop();
    }

    @Test
    public void testStartAndStop () {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.start();
        client.stop();
    }

}
