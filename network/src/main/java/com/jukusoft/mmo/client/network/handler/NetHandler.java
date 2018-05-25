package com.jukusoft.mmo.client.network.handler;

import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.network.NClient;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public interface NetHandler {

    public void handle(Buffer content, NClient client, WritableGame game);

}
