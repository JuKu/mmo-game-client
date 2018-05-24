package com.jukusoft.mmo.client.gui.screens.impl.init;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.gui.assetmanager.GameAssetManager;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;

public class SelectServerScreen implements IScreen {

    protected Stage stage = null;
    protected GameAssetManager assetManager = GameAssetManager.getInstance();

    protected String bgPath = "";
    protected String logoPath = "";

    //images
    protected Image screenBG = null;
    protected Image logo = null;

    @Override
    public void onStart(Game game) {
        //read image paths from config
        Profile.Section section = null;

        try {
            Ini ini = new Ini(new File("./config/graphics.cfg"));
            section = ini.get("SelectServer");
        } catch (IOException e) {
            LocalLogger.printStacktrace(e);
            LocalLogger.print("close application now.");
            System.exit(1);
        }

        this.bgPath = section.get("background");
        this.logoPath = section.get("logo");

        //create UI stage
        this.stage = new Stage();
    }

    @Override
    public void onStop(Game game) {

    }

    @Override
    public void onResume(Game game) {
        //load texures
        assetManager.load(this.bgPath, Texture.class);
        assetManager.load(this.logoPath, Texture.class);

        assetManager.finishLoading();

        Texture bgTexture = assetManager.get(this.bgPath, Texture.class);
        this.screenBG = new Image(bgTexture);

        Texture logoTexture = assetManager.get(this.logoPath, Texture.class);
        this.logo = new Image(logoTexture);

        //add widgets to stage
        stage.addActor(screenBG);
        stage.addActor(logo);
    }

    @Override
    public void onPause(Game game) {
        assetManager.unload(this.bgPath);
        assetManager.unload(this.logoPath);
    }

    @Override
    public void onResize(int width, int height) {
        stage.getViewport().setScreenWidth(width);
        stage.getViewport().setScreenHeight(height);

        //make the background fill the screen
        screenBG.setSize(width, height);

        //place the logo in the middle of the screen and 100 px up
        logo.setX((width - logo.getWidth()) / 2);
        logo.setY(200);
        //logo.setY((height - logo.getHeight()) / 2 + 100);
    }

    @Override
    public boolean processInput(Game game, ScreenManager<IScreen> screenManager) {
        return false;
    }

    @Override
    public void update(Game game, ScreenManager<IScreen> screenManager) {

    }

    @Override
    public void draw(Game game) {
        //show the loading screen
        stage.act();
        stage.draw();
    }

}
