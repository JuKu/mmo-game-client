package com.jukusoft.mmo.client.game.character;

import io.vertx.core.json.JsonObject;

public class CharacterSlot {

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

    protected CharacterSlot(final int cid, final String name, final GENDER gender, String skinColor, String hairColor, String hairStyle, String beart) {
        if (cid <= 0) {
            throw new IllegalArgumentException("cid has to be greater than 0.");
        }

        if (name == null) {
            throw new NullPointerException("name cannot be null.");
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty.");
        }

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
    public static CharacterSlot createFromJSON (JsonObject json) {
        int cid = json.getInteger("cid");
        String name = json.getString("name");
        GENDER gender = (json.getString("gender").equals("male") ? GENDER.MALE : GENDER.FEMALE);
        String skinColor = json.getString("skinColor");
        String hairColor = json.getString("hairColor");
        String hairStyle = json.getString("hairStyle");
        String beart = json.getString("beart");

        return new CharacterSlot(cid, name, gender, skinColor, hairColor, hairStyle, beart);
    }

}
