package com.jukusoft.mmo.client.game;

import com.jukusoft.mmo.client.game.region.WritableRegion;

public interface WritableGame extends Game {

    /**
    * get writable region for network module
    */
    public WritableRegion getWritableRegion ();

}
