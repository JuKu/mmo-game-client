package com.jukusoft.mmo.client.game;

import com.jukusoft.mmo.client.game.character.CharacterSlot;
import com.jukusoft.mmo.client.game.character.CharacterSlots;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.region.Region;

public interface Game {

    /**
    * get current server
    */
    public ServerManager.Server getCurrentServer ();

    /**
     * get region, e.q. to show region title on HUD (head-up display)
     */
    public Region getRegion ();

    /**
    * get network ping
     *
     * @return network ping to proxy server
    */
    public int getPing ();

    /**
    * get selected character
    */
    public CharacterSlot getSelectedCharacter ();

    /**
    * list all available characters of player
    */
    public CharacterSlots getCharacterSlots();

}
