package com.jukusoft.mmo.client.gui.screens.impl.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jukusoft.mmo.client.engine.fps.FPSManager;
import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.utils.Platform;
import com.jukusoft.mmo.client.engine.version.Version;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.game.character.CharacterSlot;
import com.jukusoft.mmo.client.game.config.Config;
import com.jukusoft.mmo.client.gui.assetmanager.GameAssetManager;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;
import com.jukusoft.mmo.client.gui.screens.Screens;
import com.jukusoft.mmo.client.gui.utils.SkinFactory;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;

public class SelectCharacterScreen implements IScreen {

    protected Stage stage = null;
    protected GameAssetManager assetManager = GameAssetManager.getInstance();
    protected Skin skin = null;
    protected Skin skin2 = null;
    protected Skin skin3 = null;
    protected ScreenManager<IScreen> screenManager = null;
    protected Pixmap labelColor = null;
    protected Pixmap hintLabelColor = null;

    protected boolean loaded = false;

    //texture paths
    protected String bgPath = "";
    protected String logoPath = "";
    protected String slotBGPath = "";
    protected String slotBGHoveredPath = "";
    protected String newSlotPath = "";
    protected String newSlotHoverPath = "";

    //images
    protected Image screenBG = null;
    protected Image logo = null;

    //labels
    protected Label versionLabel = null;
    protected Label pingLabel = null;
    protected Label fpsLabel = null;
    protected Label hintLabel = null;

    //skin paths
    String skinJsonFile = "";

    protected Texture slotBG = null;
    protected Texture slotBGHover = null;
    protected Texture newSlotBG = null;
    protected Texture newSlotHoverBG = null;

    protected Button[] slots = new Button[Config.MAX_CHARACTER_SLOTS];

    protected Game game = null;

    @Override
    public void onStart(Game game, ScreenManager<IScreen> screenManager) {
        this.game = game;
        this.screenManager = screenManager;

        //read image paths from config
        Profile.Section section = null;
        Profile.Section skinSection = null;

        try {
            Ini ini = new Ini(new File("./config/graphics.cfg"));
            section = ini.get("SelectCharacter");
            skinSection = ini.get("UI.Skin");
        } catch (IOException e) {
            LocalLogger.printStacktrace(e);
            LocalLogger.print("close application now.");
            System.exit(1);
        }

        this.bgPath = section.get("background");
        this.logoPath = section.get("logo");
        this.slotBGPath = section.get("slotBackground");
        this.slotBGHoveredPath = section.get("slotBackgroundHover");
        this.newSlotPath = section.get("newSlot");
        this.newSlotHoverPath = section.get("newSlotHover");
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
        this.loaded = false;

        //create skin
        this.skin = SkinFactory.createSkin(this.skinJsonFile);

        this.skin2 = SkinFactory.createSkin("./data/misc/skins/libgdx/uiskin.json");
        this.skin3 = SkinFactory.createSkin("./data/misc/skins/libgdx/uiskin.json");

        //load texures
        assetManager.load(this.bgPath, Texture.class);
        assetManager.load(this.logoPath, Texture.class);
        assetManager.load(this.slotBGPath, Texture.class);
        assetManager.load(this.slotBGHoveredPath, Texture.class);
        assetManager.load(this.newSlotPath, Texture.class);
        assetManager.load(this.newSlotHoverPath, Texture.class);

        assetManager.finishLoading();

        Texture bgTexture = assetManager.get(this.bgPath, Texture.class);
        this.screenBG = new Image(bgTexture);

        Texture logoTexture = assetManager.get(this.logoPath, Texture.class);
        this.logo = new Image(logoTexture);

        this.slotBG = assetManager.get(this.slotBGPath, Texture.class);
        this.slotBGHover = assetManager.get(this.slotBGHoveredPath, Texture.class);
        this.newSlotBG = assetManager.get(this.newSlotPath, Texture.class);
        this.newSlotHoverBG = assetManager.get(this.newSlotHoverPath, Texture.class);

        //add widgets to stage
        stage.addActor(screenBG);
        //stage.addActor(logo);

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
        assetManager.unload(this.logoPath);
        assetManager.unload(this.slotBGPath);
        assetManager.unload(this.slotBGHoveredPath);
        assetManager.unload(this.newSlotPath);
        assetManager.unload(this.newSlotHoverPath);

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

        float startY = (height - 100) / 2f + 200f;

        for (int i = 0; i < Config.MAX_CHARACTER_SLOTS; i++) {
            if (this.slots[i] == null) {
                continue;
            }

            this.slots[i].setX((width - slots[i].getWidth()) / 2);
            this.slots[i].setY(startY - (i * 110));
        }
    }

    /**
    * add slots to ui
     *
     * this method is called if character list response was received from proxy server
    */
    protected void init (CharacterSlot[] slots) {
        for (int i = 0; i < Config.MAX_CHARACTER_SLOTS; i++) {
            Drawable drawable = new TextureRegionDrawable(new TextureRegion(this.slotBG, this.slotBG.getWidth(), this.slotBG.getHeight()));
            Drawable drawable_hover = new TextureRegionDrawable(new TextureRegion(this.slotBGHover, this.slotBG.getWidth(), this.slotBG.getHeight()));

            if (slots[i] == null) {
                //slot is empty
                drawable = new TextureRegionDrawable(new TextureRegion(this.newSlotBG, this.newSlotBG.getWidth(), this.newSlotBG.getHeight()));
                drawable_hover = new TextureRegionDrawable(new TextureRegion(this.newSlotHoverBG, this.newSlotBG.getWidth(), this.newSlotBG.getHeight()));

                ImageButton btn = new ImageButton(drawable, drawable_hover, drawable_hover);
                btn.getStyle().over = drawable_hover;
                btn.getStyle().imageOver = drawable_hover;
                btn.getStyle().checkedOver = drawable_hover;
                btn.getStyle().imageCheckedOver = drawable_hover;

                this.slots[i] = btn;
            } else {
                TextButton btn = new TextButton(slots[i].getName(), this.skin);
                btn.getStyle().up = drawable;
                btn.getStyle().down = drawable_hover;
                btn.getStyle().over = drawable_hover;
                btn.getStyle().checked = drawable_hover;
                btn.getStyle().checkedOver = drawable_hover;

                btn.setWidth(this.slotBG.getWidth());
                btn.setHeight(this.slotBG.getHeight());

                this.slots[i] = btn;
            }

            this.slots[i].setChecked(false);

            if (slots[i] == null) {
                this.slots[i].addListener(new ClickListener() {
                    @Override
                    public void clicked (InputEvent event, float x, float y) {
                        //go to create character screen
                        screenManager.leaveAllAndEnter(Screens.CREATE_CHARACTER);
                    }
                });
            } else {
                final int slotID = i;

                this.slots[i].addListener(new ClickListener() {
                    @Override
                    public void clicked (InputEvent event, float x, float y) {
                        //select character
                        game.getCharacterSlots().selectCharacterSlot(slots[slotID], res -> {
                            if (res) {
                                LocalLogger.print("character selected successfully.");

                                //go to loading region screen
                                //Platform.runOnUIThread(() -> screenManager.leaveAllAndEnter(Screens.LOAD_REGION));

                                Platform.runOnUIThread(() -> {
                                    //hide all buttons, except selected region

                                    for (int i = 0; i < SelectCharacterScreen.this.slots.length; i++) {
                                        if (i != slotID) {
                                            //hide button
                                            SelectCharacterScreen.this.slots[i].setVisible(false);
                                        }
                                    }

                                    SelectCharacterScreen.this.slots[slotID].setDisabled(true);

                                    if (SelectCharacterScreen.this.slots[slotID] instanceof TextButton) {
                                        TextButton btn1 = (TextButton) SelectCharacterScreen.this.slots[slotID];
                                        btn1.setText("Loading...");
                                    }
                                });
                            } else {
                                LocalLogger.warn("Cannot select character slot " + slotID);
                            }
                        });
                    }
                });
            }

            stage.addActor(this.slots[i]);
        }

        this.onResize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

        if (!loaded) {
            if (game.getCharacterSlots().isLoaded()) {
                init(game.getCharacterSlots().getSlots());

                loaded = true;
            } else {
                return;
            }
        }
    }

    @Override
    public void draw(Game game) {
        //show the character selection screen
        stage.act();
        stage.draw();
    }
    
}
