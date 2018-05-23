package com.jukusoft.mmo.client.gui.screens.impl;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jukusoft.mmo.client.engine.cache.Cache;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.gui.assetmanager.GameAssetManager;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;

public class LoadingScreen implements IScreen {

    protected Stage stage;

    protected Image logo;
    protected Image loadingFrame;
    protected Image loadingBarHidden;
    protected Image screenBg;
    protected Image loadingBg;

    protected float startX, endX;
    protected float percent;

    protected Actor loadingBar;

    protected GameAssetManager assetManager = GameAssetManager.getInstance();

    @Override
    public void onStart(Game game) {
        // Tell the manager to load assets for the loading screen
        assetManager.load(Cache.getInstance().getPath() + "assets/loading/loading.pack.atlas", TextureAtlas.class);
    }

    @Override
    public void onStop(Game game) {

    }

    @Override
    public void onResume(Game game) {

    }

    @Override
    public void onPause(Game game) {

    }

    @Override
    public boolean processInput(Game game, ScreenManager<IScreen> screenManager) {
        return false;
    }

    @Override
    public void update(Game game, ScreenManager<IScreen> screenManager) {
        //
    }

    @Override
    public void draw(Game game) {
        // Interpolate the percentage to make it more smooth
        /*percent = Interpolation.linear.apply(percent, game.manager.getProgress(), 0.1f);

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
        stage.draw();*/
    }

}
