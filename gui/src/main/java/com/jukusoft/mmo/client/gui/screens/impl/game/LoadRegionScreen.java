package com.jukusoft.mmo.client.gui.screens.impl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jukusoft.mmo.client.engine.cache.Cache;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.gui.assetmanager.GameAssetManager;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;
import com.jukusoft.mmo.client.gui.screens.impl.loading.LoadingBar;
import com.jukusoft.mmo.client.gui.utils.SkinFactory;

import java.io.File;

public class LoadRegionScreen implements IScreen {

    protected Stage stage;

    protected GameAssetManager assetManager = GameAssetManager.getInstance();

    //paths
    protected static final String WALLPAPER_PATH = new File("data/misc/placeholders/loading_region.png").getAbsolutePath().replace("\\", "/");
    protected static final String TEXTURE_ATLAS_PATH = Cache.getInstance().getPath() + "assets/loading/loading.pack.atlas";
    protected static final String SKIN_JSON_PATH = "data/misc/skins/default/uiskin.json";

    //background texture
    protected Texture bgTexture = null;

    //skins
    protected Skin skin = null;

    //widgets
    protected Image logo;
    protected Image loadingFrame;
    protected Image loadingBarHidden;
    protected Image screenBg;
    protected Image loadingBg;
    protected Label label;

    //loading bar
    protected Actor loadingBar;

    protected float startX = 0;
    protected float endX = Gdx.graphics.getBackBufferWidth();
    protected float percent = 0;

    @Override
    public void onStart(Game game, ScreenManager<IScreen> screenManager) {
        this.stage = new Stage();
    }

    @Override
    public void onStop(Game game) {
        this.stage.dispose();
        this.stage = null;
    }

    @Override
    public void onResume(Game game) {
        //load assets
        assetManager.load(WALLPAPER_PATH, Texture.class);
        assetManager.load(TEXTURE_ATLAS_PATH, TextureAtlas.class);

        //wait until they are finished loading
        assetManager.finishLoading();

        //get texture
        this.bgTexture = assetManager.get(WALLPAPER_PATH, Texture.class);

        // Get our textureatlas from the manager
        TextureAtlas atlas = assetManager.get(TEXTURE_ATLAS_PATH, TextureAtlas.class);

        //create skin
        this.skin = SkinFactory.createSkin(SKIN_JSON_PATH);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("logo_large"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        screenBg = new Image(this.bgTexture);
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        //add label with region title
        this.label = new Label("  Region: " + game.getRegion().getTitle() + "  ", this.skin);
        this.label.getStyle().background = new TextureRegionDrawable(atlas.findRegion("label"));
        this.label.setHeight(50);
        this.label.invalidate();

        // Add the loading bar animation
        Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim") );
        anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        loadingBar = new LoadingBar(anim);

        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);
        stage.addActor(label);

        //dont accept input while loading region
        Gdx.input.setInputProcessor(null);

    }

    @Override
    public void onPause(Game game) {
        assetManager.unload(WALLPAPER_PATH);
        assetManager.unload(TEXTURE_ATLAS_PATH);

        this.skin.dispose();
        this.skin = null;
    }

    @Override
    public void onResize(int width, int height) {
        stage.getViewport().setScreenWidth(width);
        stage.getViewport().setScreenHeight(height);

        // Make the background fill the screen
        screenBg.setSize(width, height);

        float yOffset = 100;

        // Place the logo in the middle of the screen and 100 px up
        logo.setX((width - logo.getWidth()) / 2);
        logo.setY((height - logo.getHeight()) / 2 + yOffset);

        float loadingBarOffsetY = /*-120*//*-200*/-210;

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2 + loadingBarOffsetY);

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

        label.setX((width - label.getWidth()) / 2);
        label.setY((height - label.getHeight()) / 2 + yOffset + 200);
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
        //interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, this.getCurrentProgress(), 0.1f);

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

    protected float getCurrentProgress () {
        return 0.1f;
    }

}
