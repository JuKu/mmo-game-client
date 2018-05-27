package com.jukusoft.mmo.client.gui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.client.engine.fps.FPSManager;
import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.time.GameTime;
import com.jukusoft.mmo.client.engine.utils.Platform;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.gui.assetmanager.GameAssetManager;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;
import com.jukusoft.mmo.client.gui.screens.Screens;
import com.jukusoft.mmo.client.gui.screens.impl.DefaultScreenManager;
import com.jukusoft.mmo.client.gui.screens.impl.character.SelectCharacterScreen;
import com.jukusoft.mmo.client.gui.screens.impl.init.SelectServerScreen;
import com.jukusoft.mmo.client.gui.screens.impl.loading.LoadingScreen;
import com.jukusoft.mmo.client.gui.screens.impl.login.LoginScreen;

public class GameGUI implements ApplicationListener {

    protected final Game game;
    protected final ScreenManager<IScreen> screenManager;
    protected final GameTime time = GameTime.getInstance();
    protected final FPSManager fps = FPSManager.getInstance();
    protected final GameAssetManager assetManager = GameAssetManager.getInstance();

    //window background (clear) color
    protected Color bgColor = Color.BLACK;

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
        //add screens
        this.screenManager.addScreen(Screens.LOADING_SCREEN, new LoadingScreen());
        this.screenManager.addScreen(Screens.SELECT_SERVER_SCREEN, new SelectServerScreen());
        this.screenManager.addScreen(Screens.LOGIN_SCREEN, new LoginScreen());
        this.screenManager.addScreen(Screens.CHARACTER_SELECTION, new SelectCharacterScreen());

        //activate screen
        this.screenManager.leaveAllAndEnter(Screens.LOADING_SCREEN);
    }

    @Override
    public void resize(int width, int height) {
        this.screenManager.resize(width, height);
    }

    @Override
    public void render() {
        //first, update game time
        this.time.setTime(System.currentTimeMillis());
        this.time.setDelta(Gdx.graphics.getDeltaTime());

        //update FPS
        fps.setFPS(Gdx.graphics.getFramesPerSecond());

        //show FPS warning, if neccessary
        fps.showWarningIfNeccessary();

        //execute tasks, which should be executed in OpenGL context thread
        Platform.executeQueue();

        //load assets
        assetManager.update();

        try {
            //process input
            this.screenManager.processInput();

            //update screens
            this.screenManager.update();

            //clear all color buffer bits and clear screen
            Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            //draw screens
            this.screenManager.draw();
        } catch (Exception e) {
            LocalLogger.printStacktrace(e);
            Gdx.app.error("BaseGame", "exception thrown while render game: " + e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        this.screenManager.dispose();
    }

}
