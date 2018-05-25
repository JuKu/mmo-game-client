package com.jukusoft.mmo.client.gui.screens.impl.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jukusoft.mmo.client.engine.cache.Cache;
import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.time.GameTime;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.gui.assetmanager.GameAssetManager;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;
import com.jukusoft.mmo.client.gui.screens.Screens;
import com.jukusoft.mmo.client.gui.utils.TexturePackerHelper;

import java.io.File;
import java.io.IOException;

public class LoadingScreen implements IScreen {

    protected Stage stage;

    protected Image logo;
    protected Image loadingFrame;
    protected Image loadingBarHidden;
    protected Image screenBg;
    protected Image loadingBg;

    protected float startX = 0;
    protected float endX = Gdx.graphics.getBackBufferWidth();
    protected float percent;

    protected Actor loadingBar;

    protected GameAssetManager assetManager = GameAssetManager.getInstance();

    protected static final String TEXTURE_ATLAS_PATH = Cache.getInstance().getPath() + "assets/loading/loading.pack.atlas";

    protected float elapsed = 0;
    protected volatile boolean finished = false;

    //https://github.com/Matsemann/libgdx-loading-screen/blob/master/Main/src/com/matsemann/libgdxloadingscreen/screen/LoadingScreen.java

    @Override
    public void onStart(Game game, ScreenManager<IScreen> screenManager) {
        //tell the manager to load assets for the loading screen
        assetManager.load(TEXTURE_ATLAS_PATH, TextureAtlas.class);

        //wait until they are finished loading
        assetManager.finishLoading();

        // Initialize the stage where we will place everything
        stage = new Stage();

        // Get our textureatlas from the manager
        TextureAtlas atlas = assetManager.get(TEXTURE_ATLAS_PATH, TextureAtlas.class);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("libgdx-logo"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        screenBg = new Image(atlas.findRegion("screen-bg"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        // Add the loading bar animation
        Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim") );
        anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        loadingBar = new LoadingBar(anim);

        // Or if you only need a static bar, you can do
        // loadingBar = new Image(atlas.findRegion("loading-bar1"));

        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);

        //TODO: load other resources

        Thread thread = new Thread(() -> {
            try {
                TexturePackerHelper.packTextures(new File("./data/packer/packer.json"));
                finished = true;
            } catch (IOException e) {
                LocalLogger.printStacktrace(e);
            }
        });
        thread.start();
    }

    @Override
    public void onStop(Game game) {
        // Dispose the loading assets as we no longer need them
        assetManager.unload(Cache.getInstance().getPath() + "assets/loading/loading.pack.atlas");
    }

    @Override
    public void onResume(Game game) {

    }

    @Override
    public void onPause(Game game) {

    }

    @Override
    public void onResize(int width, int height) {
        // Set our screen to always be XXX x 480 in size
        width = 720 * width / height;
        height = 720;
        stage.getViewport().setScreenWidth(width);
        stage.getViewport().setScreenHeight(height);

        // Make the background fill the screen
        screenBg.setSize(width, height);

        // Place the logo in the middle of the screen and 100 px up
        logo.setX((width - logo.getWidth()) / 2);
        logo.setY((height - logo.getHeight()) / 2 + 100);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame, adjusted a few px
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the image that will hide the bar on top of the bar, adjusted a few px
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingBarHidden.getX();
        endX = 440;

        // The rest of the hidden bar
        loadingBg.setSize(450, 50);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() + 3);
    }

    @Override
    public boolean processInput(Game game, ScreenManager<IScreen> screenManager) {
        return false;
    }

    @Override
    public void update(Game game, ScreenManager<IScreen> screenManager) {
        if (assetManager.getProgress() >= 1f) {
            elapsed += GameTime.getInstance().getDelta();

            //wait minimum 2 seconds
            if (elapsed > 1 && finished) {
                screenManager.leaveAllAndEnter(Screens.SELECT_SERVER_SCREEN);
                screenManager.removeScreen(Screens.LOADING_SCREEN);
            }
        }
    }

    @Override
    public void draw(Game game) {
        //interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, assetManager.getProgress(), 0.1f);

        //update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);

        //invalidate image, because size has changed
        loadingBg.invalidate();

        //show the loading screen
        stage.act();
        stage.draw();
    }

}
