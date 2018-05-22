package com.jukusoft.mmo.client.gui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.jukusoft.mmo.client.engine.time.GameTime;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;
import com.jukusoft.mmo.client.gui.screens.impl.DefaultScreenManager;

public class GameGUI implements ApplicationListener {

    protected final Game game;
    protected final ScreenManager<IScreen> screenManager;
    protected GameTime time = GameTime.getInstance();

    /**
    * default constructor
     *
     * @param game instance of game
    */
    public GameGUI (Game game) {
        this.game = game;
        this.screenManager = new DefaultScreenManager(this.game);
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        //first, update game time
        this.time.setTime(System.currentTimeMillis());
        this.time.setDelta(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}
