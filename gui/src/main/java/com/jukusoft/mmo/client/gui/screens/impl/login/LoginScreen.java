package com.jukusoft.mmo.client.gui.screens.impl.login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

public class LoginScreen implements IScreen {

    protected Stage stage = null;
    protected GameAssetManager assetManager = GameAssetManager.getInstance();
    protected Skin skin = null;
    protected Skin skin2 = null;
    protected ScreenManager<IScreen> screenManager = null;
    protected Pixmap labelColor = null;

    //texture paths
    protected String bgPath = "";
    protected String logoPath = "";

    //images
    protected Image screenBG = null;
    protected Image logo = null;

    //labels
    protected Label versionLabel = null;
    protected Label pingLabel = null;

    //widgets
    protected TextField usernameTextField = null;
    protected TextField passwordTextField = null;
    protected TextButton loginButton = null;

    @Override
    public void onStart(Game game, ScreenManager<IScreen> screenManager) {
        this.screenManager = screenManager;

        //read image paths from config
        Profile.Section section = null;
        Profile.Section skinSection = null;

        try {
            Ini ini = new Ini(new File("./config/graphics.cfg"));
            section = ini.get("SelectServer");
            skinSection = ini.get("UI.Skin");
        } catch (IOException e) {
            LocalLogger.printStacktrace(e);
            LocalLogger.print("close application now.");
            System.exit(1);
        }

        this.bgPath = section.get("background");
        this.logoPath = section.get("logo");

        //create skin
        String atlasFile = skinSection.get("atlas");
        String jsonFile = skinSection.get("json");
        LocalLogger.print("create skin, atlas file: " + atlasFile + ", json file: " + jsonFile);
        this.skin = SkinFactory.createSkin(jsonFile);

        this.skin2 = SkinFactory.createSkin("./data/misc/skins/libgdx/uiskin.json");

        //create UI stage
        this.stage = new Stage();
    }

    @Override
    public void onStop(Game game) {
        this.skin.dispose();
        this.skin = null;

        this.skin2.dispose();
        this.skin2 = null;
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

        //get client version
        Version version = Version.getInstance();

        this.versionLabel = new Label("Version: " + version.getFullVersion(), this.skin2);

        //set label background color
        labelColor = new Pixmap((int) this.versionLabel.getWidth(), (int) this.versionLabel.getHeight(), Pixmap.Format.RGBA8888);
        labelColor.setColor(Color.valueOf("#36581a"));
        labelColor.fill();
        this.versionLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        stage.addActor(versionLabel);

        //ping label
        this.pingLabel = new Label("Ping: n/a", this.skin2);
        this.pingLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        stage.addActor(pingLabel);

        //text fields
        this.usernameTextField = new TextField("Username", this.skin2);
        stage.addActor(usernameTextField);

        this.passwordTextField = new TextField("Password", this.skin2);
        this.passwordTextField.setPasswordMode(true);
        this.passwordTextField.setPasswordCharacter('*');
        stage.addActor(passwordTextField);

        this.loginButton = new TextButton("Login", this.skin);
        stage.addActor(loginButton);

        //set input processor
        Gdx.input.setInputProcessor(stage);
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
        logo.setY((height - logo.getHeight()) / 2 + 200);

        versionLabel.setX(20);
        versionLabel.setY(20);

        pingLabel.setX(20);
        pingLabel.setY(height - 50);

        float startY = (height - usernameTextField.getHeight()) / 2;

        usernameTextField.setWidth(400);
        usernameTextField.setX((width - usernameTextField.getWidth()) / 2);
        usernameTextField.setY(startY);

        passwordTextField.setWidth(400);
        passwordTextField.setX((width - passwordTextField.getWidth()) / 2);
        passwordTextField.setY(startY - 50);

        loginButton.setWidth(200);
        loginButton.setX((width - loginButton.getWidth()) / 2);
        loginButton.setY(startY - 100);

        //invalidate widgets, because width and height was changed
        usernameTextField.invalidate();
        passwordTextField.invalidate();
        loginButton.invalidate();
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
