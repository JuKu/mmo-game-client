package com.jukusoft.mmo.client.gui.screens.impl.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jukusoft.mmo.client.engine.fps.FPSManager;
import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.version.Version;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.gui.assetmanager.GameAssetManager;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;
import com.jukusoft.mmo.client.gui.utils.SkinFactory;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;

public class CreateCharacterScreen implements IScreen {

    protected Stage stage = null;
    protected GameAssetManager assetManager = GameAssetManager.getInstance();
    protected Skin skin = null;
    protected Skin skin2 = null;
    protected Skin skin3 = null;
    protected ScreenManager<IScreen> screenManager = null;
    protected Pixmap labelColor = null;
    protected Pixmap hintLabelColor = null;

    //texture paths
    protected String bgPath = "";
    protected String newCharacterBGPath = "";

    //images
    protected Image screenBG = null;
    protected Label newCharacterLabel = null;

    //labels
    protected Label versionLabel = null;
    protected Label pingLabel = null;
    protected Label fpsLabel = null;
    protected Label hintLabel = null;

    //skin paths
    String skinJsonFile = "";

    @Override
    public void onStart(Game game, ScreenManager<IScreen> screenManager) {
        this.screenManager = screenManager;

        //read image paths from config
        Profile.Section section = null;
        Profile.Section skinSection = null;

        try {
            Ini ini = new Ini(new File("./config/graphics.cfg"));
            section = ini.get("CreateCharacter");
            skinSection = ini.get("UI.Skin");
        } catch (IOException e) {
            LocalLogger.printStacktrace(e);
            LocalLogger.print("close application now.");
            System.exit(1);
        }

        this.bgPath = section.get("background");
        this.newCharacterBGPath = section.get("newCharacterBackground");
        this.skinJsonFile = skinSection.get("json");

        //create UI stage
        this.stage = new Stage();
    }

    @Override
    public void onStop(Game game) {
        this.stage.dispose();
        this.stage = null;
    }

    @Override
    public void onResume(Game game) {
        //create skin
        this.skin = SkinFactory.createSkin(this.skinJsonFile);

        this.skin2 = SkinFactory.createSkin("./data/misc/skins/libgdx/uiskin.json");
        this.skin3 = SkinFactory.createSkin("./data/misc/skins/libgdx/uiskin.json");

        //load texures
        assetManager.load(this.bgPath, Texture.class);
        assetManager.load(this.newCharacterBGPath, Texture.class);

        assetManager.finishLoading();

        Texture bgTexture = assetManager.get(this.bgPath, Texture.class);
        this.screenBG = new Image(bgTexture);

        Texture newCharacterBGTexture = assetManager.get(this.newCharacterBGPath, Texture.class);
        this.newCharacterLabel = new Label("     Create new Character ", this.skin);
        this.newCharacterLabel.setWidth(newCharacterBGTexture.getWidth());
        this.newCharacterLabel.setHeight(newCharacterBGTexture.getHeight());
        this.newCharacterLabel.getStyle().background = new TextureRegionDrawable(new TextureRegion(newCharacterBGTexture, newCharacterBGTexture.getWidth(), newCharacterBGTexture.getHeight()));
        this.newCharacterLabel.invalidate();

        //add widgets to stage
        stage.addActor(screenBG);
        stage.addActor(newCharacterLabel);

        //get client version
        Version version = Version.getInstance();

        this.versionLabel = new Label("Version: " + version.getFullVersion(), this.skin2);

        //set label background color
        labelColor = new Pixmap((int) this.versionLabel.getWidth() + 100, (int) this.versionLabel.getHeight(), Pixmap.Format.RGBA8888);
        labelColor.setColor(Color.valueOf("#36581a"));
        labelColor.fill();
        this.versionLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        stage.addActor(versionLabel);

        //ping label
        this.pingLabel = new Label("Ping: n/a", this.skin2);
        this.pingLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        stage.addActor(pingLabel);

        //fps label
        this.fpsLabel = new Label("FPS: n/a", this.skin2);
        this.fpsLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        stage.addActor(fpsLabel);

        //hint label (e.q. for error messages)
        this.hintLabel = new Label("Hints", this.skin3);
        this.hintLabel.setWidth(400);
        this.hintLabel.getStyle().fontColor = Color.RED;

        //set label background color
        hintLabelColor = new Pixmap((int) this.versionLabel.getWidth() + 100, (int) this.versionLabel.getHeight(), Pixmap.Format.RGBA8888);
        hintLabelColor.setColor(Color.valueOf("#FFFFFF"));
        hintLabelColor.fill();
        this.hintLabel.getStyle().background = new Image(new Texture(hintLabelColor)).getDrawable();

        stage.addActor(hintLabel);

        //hide hint label
        this.hintLabel.setVisible(false);

        //set input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void onPause(Game game) {
        this.skin.dispose();
        this.skin = null;

        this.skin2.dispose();
        this.skin2 = null;

        this.skin3.dispose();
        this.skin3 = null;

        assetManager.unload(this.bgPath);
        assetManager.unload(this.newCharacterBGPath);

        this.labelColor.dispose();
        this.labelColor = null;
        this.hintLabelColor.dispose();
        this.hintLabelColor = null;
    }

    @Override
    public void onResize(int width, int height) {
        stage.getViewport().setScreenWidth(width);
        stage.getViewport().setScreenHeight(height);

        //make the background fill the screen
        screenBG.setSize(width, height);

        //place the logo in the middle of the screen and 100 px up
        newCharacterLabel.setX((width - newCharacterLabel.getWidth()) / 2);
        newCharacterLabel.setY((height - newCharacterLabel.getHeight()) / 2 + 250);

        versionLabel.setX(20);
        versionLabel.setY(20);

        pingLabel.setX(20);
        pingLabel.setY(height - 50f);

        fpsLabel.setX(20);
        fpsLabel.setY(height - 80f);

        hintLabel.setX((width - hintLabel.getWidth()) / 2);
        hintLabel.setY((height - hintLabel.getHeight()) / 2 - 200);
    }

    @Override
    public boolean processInput(Game game, ScreenManager<IScreen> screenManager) {
        return false;
    }

    @Override
    public void update(Game game, ScreenManager<IScreen> screenManager) {
        //set ping
        this.pingLabel.setText("Ping: " + game.getPing() + "");

        //set fps
        this.fpsLabel.setText("FPS: " + FPSManager.getInstance().getFPS());
    }

    @Override
    public void draw(Game game) {
        //show the character selection screen
        stage.act();
        stage.draw();
    }

}