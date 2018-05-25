package com.jukusoft.mmo.client.game.connection;

import io.vertx.core.Handler;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ServerManagerTest {

    @Test
    public void testConstructor () {
        new ServerManager();
    }

    @Test
    public void testGetInstance () {
        ServerManager.getInstance().list.clear();
        assertEquals(0, ServerManager.getInstance().listServers().size());
    }

    @Test (expected = IllegalStateException.class)
    public void testLoadNotExistentFileFromConfig () throws IOException {
        ServerManager.getInstance().loadFromConfig(new File("not-existent-file.json"));
        assertEquals(true, ServerManager.getInstance().listServers().size() > 0);
    }

    @Test
    public void testLoadFromConfig () throws IOException {
        ServerManager.getInstance().loadFromConfig(new File("../config/servers.json"));
        assertEquals(true, ServerManager.getInstance().listServers().size() > 0);
    }

    @Test (expected = IllegalStateException.class)
    public void testGetSelectedServer () throws IOException {
        ServerManager.getInstance().loadFromConfig(new File("../config/servers.json"));
        ServerManager.getInstance().selectedServer = null;
        ServerManager.getInstance().getSelectedServer();
    }

    @Test (expected = IllegalStateException.class)
    public void testNullConnect () {
        ServerManager.getInstance().connectionExecutor = null;
        ServerManager.getInstance().connect(Mockito.mock(Handler.class));
    }

    @Test (expected = IllegalStateException.class)
    public void testNullServerConnect () {
        ServerManager.getInstance().connectionExecutor = Mockito.mock(Handler.class);
        ServerManager.getInstance().connect(Mockito.mock(Handler.class));
    }

    @Test
    public void testConnect () {
        ServerManager.getInstance().connectionExecutor = Mockito.mock(Handler.class);
        ServerManager.getInstance().setSelectServer(Mockito.mock(ServerManager.Server.class));
        ServerManager.getInstance().connect(Mockito.mock(Handler.class));
    }

    @Test
    public void testGetAndSetSelectedServer () throws IOException {
        ServerManager.getInstance().loadFromConfig(new File("../config/servers.json"));
        ServerManager.getInstance().setSelectServer(ServerManager.createServer("", 80, "", "", true));
        ServerManager.getInstance().getSelectedServer();
    }

}
