package com.jukusoft.mmo.client.gui.screens;

/**
 * Screen interface - screens are responsible for drawing, not for updating your game state!
 *
 * Created by Justin on 06.02.2017.
 */
public interface IScreen {

    /**
    * method which should be executed if screen is created
    */
    public void onStart();

    /**
     * method which should be executed if screen has stopped
     */
    public void onStop();

    /**
     * method is executed, if screen is set to active state now.
     */
    public void onResume();

    /**
    * method is executed, if screen isn't active anymore
    */
    public void onPause();

    /**
    * process input
     *
     * @return true, if input was processed and no other screen has to process input anymore
    */
    public boolean processInput();

    /**
     * update game screen
     */
    public void update();

    /**
     * beforeDraw game screen
     */
    public void draw();

}
