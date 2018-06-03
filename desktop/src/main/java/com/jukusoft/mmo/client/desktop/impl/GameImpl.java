package com.jukusoft.mmo.client.desktop.impl;

import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.character.CharacterSlot;
import com.jukusoft.mmo.client.game.character.CharacterSlots;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.mode.GameMode;
import com.jukusoft.mmo.client.game.region.Region;
import com.jukusoft.mmo.client.game.region.WritableRegion;
import io.vertx.core.Handler;

import java.util.concurrent.atomic.AtomicInteger;

public class GameImpl implements WritableGame {

    protected WritableRegion region = new WritableRegion();
    protected AtomicInteger ping = new AtomicInteger(0);
    protected CharacterSlots characterSlots = new CharacterSlots();
    protected GameMode gameMode = GameMode.PLAY_MODE;
    protected Handler<Void> enterLoadRegionScreenHandler = null;

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
    public CharacterSlot getSelectedCharacter() {
        return this.characterSlots.getSelectedCharacterSlot();
    }

    @Override
    public CharacterSlots getCharacterSlots() {
        return this.characterSlots;
    }

    @Override
    public GameMode getGameMode() {
        return this.gameMode;
    }

    @Override
    public void setEnterRegionLoadScreenHandler(Handler<Void> handler) {
        this.enterLoadRegionScreenHandler = handler;
    }

    @Override
    public void setGameMode(GameMode mode) {
        this.gameMode = mode;
    }

    @Override
    public void enterRegionLoadingcreen() {
        if (this.enterLoadRegionScreenHandler == null) {
            throw new IllegalStateException("enter load region screen handler wasnt set before from GameGUI.");
        }

        this.enterLoadRegionScreenHandler.handle(null);
    }

}
