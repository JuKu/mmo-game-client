package com.jukusoft.mmo.client.network.handler;

import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.network.NClient;
import io.vertx.core.buffer.Buffer;

public interface NetHandler {

    public void handle(Buffer content, byte type, byte extendedType, NClient client, WritableGame game);

}
