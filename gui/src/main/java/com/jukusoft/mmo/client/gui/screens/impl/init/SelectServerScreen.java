package com.jukusoft.mmo.client.gui.screens.impl.init;

import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.gui.screens.IScreen;
import com.jukusoft.mmo.client.gui.screens.ScreenManager;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;

public class SelectServerScreen implements IScreen {

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

        String bgPath = section.get("background");
        String logoPath = section.get("logo");
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
    public void onResize(int width, int height) {

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

    }

}
