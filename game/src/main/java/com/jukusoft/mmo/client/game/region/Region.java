package com.jukusoft.mmo.client.game.region;

public class Region {

    protected String title = "";

    public Region () {
        //
    }

    /**
    * get title, which is drawn as region title on HUD
     *
     * @return region title
    */
    public String getTitle () {
        return this.title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

}
