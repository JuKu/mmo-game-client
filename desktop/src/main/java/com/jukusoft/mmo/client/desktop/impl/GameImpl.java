package com.jukusoft.mmo.client.desktop.impl;

import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.character.Character;
import com.jukusoft.mmo.client.game.character.CharacterManager;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.region.Region;
import com.jukusoft.mmo.client.game.region.WritableRegion;

import java.util.concurrent.atomic.AtomicInteger;

public class GameImpl implements WritableGame {

    protected WritableRegion region = new WritableRegion();
    protected AtomicInteger ping = new AtomicInteger(0);
    protected CharacterManager characterManager = new CharacterManager();

    public GameImpl () {
        //
    }

    @Override
    public WritableRegion getWritableRegion() {
        return this.region;
    }

    @Override
    public void init() {

    }

    @Override
    public ServerManager.Server getCurrentServer() {
        return ServerManager.getInstance().getSelectedServer();
    }

    @Override
    public Region getRegion() {
        return this.region;
    }

    @Override
    public int getPing() {
        return this.ping.get();
    }

    @Override
    public void setPing(int ping) {
        this.ping.set(ping);
    }

    @Override
    public Character getSelectedCharacter() {
        return this.characterManager.getSelectedCharacter();
    }

    @Override
    public CharacterManager getCharacterManager() {
        return this.characterManager;
    }

}
