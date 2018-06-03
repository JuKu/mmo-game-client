package com.jukusoft.mmo.client.game.region;

public class WritableRegion extends Region {

    public void setTitle (String title) {
        this.title = title;
    }

    public void setID (int regionID, int instanceID) {
        this.regionID = regionID;
        this.instanceID = instanceID;
    }

    /**
    * player joins new region
    */
    public void join (int regionID, int instanceID, String title) {
        if (regionID <= 0) {
            throw new IllegalArgumentException("invalide regionID: " + regionID + ", id has to be greater than 0.");
        }

        if (instanceID <= 0) {
            throw new IllegalArgumentException("invalide instanceID: " + instanceID + ", id has to be greater than 0.");
        }

        if (title == null) {
            throw new NullPointerException("title cannot be null.");
        }

        if (title.isEmpty()) {
            throw new IllegalArgumentException("title cannot be empty.");
        }

        this.regionID = regionID;
        this.instanceID = instanceID;
        this.title = title;
    }

}
