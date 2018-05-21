package com.jukusoft.mmo.client.game.connection;

import org.junit.Test;

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

    @Test
    public void testLoadFromConfig () throws IOException {
        ServerManager.getInstance().loadFromConfig(new File("../config/servers.json"));
        assertEquals(true, ServerManager.getInstance().listServers().size() > 0);
    }

}
