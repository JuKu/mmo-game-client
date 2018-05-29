package com.jukusoft.mmo.client.game.character;

import io.vertx.core.json.JsonObject;

public class Character {

    public enum GENDER {
        MALE, FEMALE
    };

    //unique id of character
    protected final int cid;

    //name of character
    protected final String name;

    protected final GENDER gender;

    //hautfarbe - templateID (?)
    protected final String skinColor;

    //hair color
    protected final String hairColor;

    //hair style (frisur)
    protected final String hairStyle;

    //beart (bart)
    protected final String beart;

    protected Character (final int cid, final String name, final GENDER gender, String skinColor, String hairColor, String hairStyle, String beart) {
        this.cid = cid;
        this.name = name;
        this.gender = gender;
        this.skinColor = skinColor;
        this.hairColor = hairColor;
        this.hairStyle = hairStyle;
        this.beart = beart;
    }

    public int getCID () {
        return cid;
    }

    public String getName () {
        return name;
    }

    public GENDER getGender () {
        return gender;
    }

    public String getSkinColor () {
        return skinColor;
    }

    public String getHairColor () {
        return hairColor;
    }

    public String getHairStyle () {
        return hairStyle;
    }

    public String getBeart () {
        return beart;
    }

    /**
    * factory method
    */
    public static Character createFromJSON (JsonObject json) {
        return null;
    }

}
