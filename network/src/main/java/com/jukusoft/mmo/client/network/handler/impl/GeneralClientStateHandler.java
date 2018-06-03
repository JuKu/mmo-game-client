package com.jukusoft.mmo.client.network.handler.impl;

import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.network.NClient;
import com.jukusoft.mmo.client.network.Protocol;
import com.jukusoft.mmo.client.network.handler.NetHandler;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public class GeneralClientStateHandler implements NetHandler {

    @Override
    public void handle(Buffer content, byte type, byte extendedType, NClient client, WritableGame game) throws Exception {
        if (extendedType == Protocol.MSG_EXTENDED_TYPE_LOAD_REGION) {
            //get regionID, instanceID & region title
            int regionID = content.getInt(Protocol.MSG_BODY_OFFSET);
            int instanceID = content.getInt(Protocol.MSG_BODY_OFFSET + 4);

            LocalLogger.print("received load region message.");

            int titleLength = content.getInt(Protocol.MSG_BODY_OFFSET + 8);
            String regionTitle = content.getString(Protocol.MSG_BODY_OFFSET + 12, Protocol.MSG_BODY_OFFSET + 12 + titleLength);

            game.getWritableRegion().setTitle(regionTitle);
            game.getWritableRegion().setID(regionID, instanceID);

            LocalLogger.print("load region " + regionID + ", instanceID: " + instanceID + " .");

            //set load region screen
            game.enterRegionLoadingcreen();
        }
    }

}
