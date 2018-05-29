package com.jukusoft.mmo.client.game;

import com.jukusoft.mmo.client.game.character.Character;
import com.jukusoft.mmo.client.game.character.CharacterManager;
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
    public Character getSelectedCharacter ();

    /**
    * list all available characters of player
    */
    public CharacterManager getCharacterManager ();

}
