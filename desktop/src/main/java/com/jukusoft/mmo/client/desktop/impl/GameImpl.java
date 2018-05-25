package com.jukusoft.mmo.client.desktop.impl;

import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.region.Region;
import com.jukusoft.mmo.client.game.region.WritableRegion;

public class GameImpl implements WritableGame {

    protected WritableRegion region = new WritableRegion();

    public GameImpl () {
        //
    }

    @Override
    public WritableRegion getWritableRegion() {
        return this.region;
    }

    @Override
    public ServerManager.Server getCurrentServer() {
        return ServerManager.getInstance().getSelectedServer();
    }

    @Override
    public Region getRegion() {
        return this.region;
    }

}
