package com.jukusoft.mmo.client.game;

import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.region.Region;
import com.jukusoft.mmo.client.game.region.WritableRegion;

public interface Game {

    /**
    * get current server
    */
    public ServerManager.Server getCurrentServer ();

    /**
     * get region, e.q. to show region title on HUD (head-up display)
     */
    public Region getRegion ();

}
