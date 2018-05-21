package com.jukusoft.mmo.client.network;

import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.game.WritableGame;
import org.junit.Test;
import org.mockito.Mockito;

public class NClientTest {

    @Test
    public void testConstructor () {
        WritableGame game = Mockito.mock(WritableGame.class);

        new NClient(game);
    }

    @Test
    public void testStartAndStop () {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.start();
        client.stop();
    }

}
