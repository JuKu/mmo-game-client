package com.jukusoft.mmo.client.gui.screens.impl.login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jukusoft.mmo.client.engine.fps.FPSManager;
import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.utils.EncryptionUtils;
import com.jukusoft.mmo.client.engine.utils.Platform;
import com.jukusoft.mmo.client.engine.version.Version;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.game.login.LoginManager;
import com.jukusoft.mmo.client.gui.assetmanager.GameAssetManager;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;
import com.jukusoft.mmo.client.gui.screens.Screens;
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
    protected Skin skin3 = null;
    protected ScreenManager<IScreen> screenManager = null;
    protected Pixmap labelColor = null;
    protected Pixmap hintLabelColor = null;

    //texture paths
    protected String bgPath = "";
    protected String logoPath = "";

    //images
    protected Image screenBG = null;
    protected Image logo = null;

    //labels
    protected Label versionLabel = null;
    protected Label pingLabel = null;
    protected Label fpsLabel = null;
    protected Label hintLabel = null;

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
        this.skin3 = SkinFactory.createSkin("./data/misc/skins/libgdx/uiskin.json");

        //create UI stage
        this.stage = new Stage();
    }

    @Override
    public void onStop(Game game) {
        this.skin.dispose();
        this.skin = null;

        this.skin2.dispose();
        this.skin2 = null;

        this.skin3.dispose();
        this.skin3 = null;
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

        //text fields
        this.usernameTextField = new TextField("", this.skin2);
        this.usernameTextField.setFocusTraversal(true);
        this.usernameTextField.setMessageText("Username");
        stage.addActor(usernameTextField);

        this.passwordTextField = new TextField("", this.skin2);
        this.passwordTextField.setPasswordMode(true);
        this.passwordTextField.setPasswordCharacter('*');
        this.passwordTextField.setFocusTraversal(true);
        this.passwordTextField.setMessageText("Password");
        stage.addActor(passwordTextField);

        this.loginButton = new TextButton("Login", this.skin);
        this.loginButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                LocalLogger.print("login button clicked.");

                //get values
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();

                //check, if username is empty
                if (username.isEmpty()) {
                    usernameTextField.getStyle().messageFontColor = Color.RED;
                    usernameTextField.invalidate();

                    return;
                }

                //check, if password is empty
                if (password.isEmpty()) {
                    passwordTextField.getStyle().messageFontColor = Color.RED;
                    passwordTextField.invalidate();

                    return;
                }

                hintLabel.setText("Login...");
                hintLabel.setVisible(true);

                //try to login
                LoginManager.getInstance().login(username, password, (LoginManager.LOGIN_RESPONSE res) -> {
                    Platform.runOnUIThread(() -> {
                        //first check, if public key is available
                        if (!EncryptionUtils.isInitialized()) {
                            hintLabel.setText(" No RSA public key received, wait some seconds and click again... ");
                            hintLabel.setVisible(true);
                            return;
                        }

                        if (res == LoginManager.LOGIN_RESPONSE.NO_SERVER) {
                            //go back to server selection
                            screenManager.leaveAllAndEnter(Screens.SELECT_SERVER_SCREEN);
                        } else if (res == LoginManager.LOGIN_RESPONSE.CLIENT_ERROR) {
                            hintLabel.setText(" Client Error! ");
                            hintLabel.setVisible(true);
                            return;
                        } else if (res == LoginManager.LOGIN_RESPONSE.WRONG_CREDENTIALS) {
                            hintLabel.setText(" Wrong credentials! ");
                            hintLabel.setVisible(true);
                            return;
                        } else if (res == LoginManager.LOGIN_RESPONSE.SUCCESSFUL) {
                            hintLabel.setVisible(false);

                            //go to character selection screen
                            screenManager.leaveAllAndEnter(Screens.CHARACTER_SELECTION);
                        }
                    });
                });
            }
        });
        stage.addActor(loginButton);

        //set input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void onPause(Game game) {
        assetManager.unload(this.bgPath);
        assetManager.unload(this.logoPath);

        this.labelColor.dispose();
        this.hintLabelColor.dispose();
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
        pingLabel.setY(height - 50f);

        fpsLabel.setX(20);
        fpsLabel.setY(height - 80f);

        hintLabel.setX((width - hintLabel.getWidth()) / 2);
        hintLabel.setY((height - hintLabel.getHeight()) / 2 - 200);

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
        hintLabel.invalidate();
        loginButton.invalidate();
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
        //show the loading screen
        stage.act();
        stage.draw();
    }

}
