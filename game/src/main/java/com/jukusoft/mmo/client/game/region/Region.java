package com.jukusoft.mmo.client.game.region;

public class Region {

    protected String title = "";
    protected int regionID = 0;
    protected int instanceID = 0;

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

    public int getRegionID() {
        return regionID;
    }

    public int getInstanceID() {
        return instanceID;
    }

}
