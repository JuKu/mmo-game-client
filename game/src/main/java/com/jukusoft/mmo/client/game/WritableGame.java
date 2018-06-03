package com.jukusoft.mmo.client.game;

import com.jukusoft.mmo.client.game.mode.GameMode;
import com.jukusoft.mmo.client.game.region.WritableRegion;

public interface WritableGame extends Game {

    /**
    * get writable region for network module
    */
    public WritableRegion getWritableRegion ();

    public void init ();

    /**
    * set network ping
     *
     * @param ping network ping to proxy server
    */
    public void setPing (int ping);

    /**
    * set game mode
    */
    public void setGameMode (GameMode mode);

    /**
    * show load region screen
    */
    public void enterRegionLoadingcreen ();

}
