package com.jukusoft.mmo.client.desktop.impl;

import com.jukusoft.mmo.client.game.connection.ServerManager;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameImplTest {

    @Test
    public void testConstructor () {
        new GameImpl();
    }

    @Test
    public void testGetterAndSetter () {
        GameImpl game = new GameImpl();
        assertNotNull(game.getRegion());
        assertNotNull(game.getWritableRegion());

        assertEquals(0, game.getPing());

        //set ping
        game.setPing(50);
        assertEquals(50, game.getPing());
    }

    @Test (expected = IllegalStateException.class)
    public void testGetNotSelectedServer () {
        GameImpl game = new GameImpl();
        game.getCurrentServer();
    }

    @Test
    public void testInit () {
        GameImpl game = new GameImpl();
        game.init();
    }

    @Test
    public void testGetCurrentServer () {
        GameImpl game = new GameImpl();
        ServerManager.getInstance().setSelectServer(Mockito.mock(ServerManager.Server.class));
        assertNotNull(game.getCurrentServer());

        ServerManager.getInstance().setSelectServer(null);
    }

}
