package com.jukusoft.mmo.client.desktop.impl;

import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.mode.GameMode;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

        //set game mode
        game.setGameMode(GameMode.OBSERVE_MODE);
        assertEquals(GameMode.OBSERVE_MODE, game.getGameMode());

        game.setGameMode(GameMode.PLAY_MODE);
        assertEquals(GameMode.PLAY_MODE, game.getGameMode());
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

    @Test
    public void testGetSelectedCharacter () {
        GameImpl game = new GameImpl();
        assertNull(game.getSelectedCharacter());
    }

    @Test
    public void testGetCharacterManager () {
        GameImpl game = new GameImpl();
        assertNotNull(game.getCharacterSlots());
    }

}
