package com.jukusoft.mmo.client.desktop.impl;

import org.junit.Test;

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
    }

    @Test (expected = IllegalStateException.class)
    public void testGetNotSelectedServer () {
        GameImpl game = new GameImpl();
        game.getCurrentServer();
    }

}
