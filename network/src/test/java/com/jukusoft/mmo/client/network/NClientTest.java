package com.jukusoft.mmo.client.network;

import com.jukusoft.mmo.client.game.WritableGame;
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
    public void testStartAndStop () {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.start();
        client.stop();
    }

}
