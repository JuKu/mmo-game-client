package com.jukusoft.mmo.client.game.character;

import io.vertx.core.json.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CharacterSlotTest {

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullCID () {
        new CharacterSlot(0, "name", CharacterSlot.GENDER.MALE, "", "", "", "");
    }

    @Test (expected = NullPointerException.class)
    public void testConstructorNullName () {
        new CharacterSlot(1, null, CharacterSlot.GENDER.MALE, "", "", "", "");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorEmptyName () {
        new CharacterSlot(1, "", CharacterSlot.GENDER.MALE, "", "", "", "");
    }

    @Test
    public void testGetter () {
        CharacterSlot slot = new CharacterSlot(10, "name", CharacterSlot.GENDER.MALE, "skinColor", "hairColor", "hairStyle", "beart");

        assertEquals(10, slot.getCID());
        assertEquals("name", slot.getName());
        assertEquals(CharacterSlot.GENDER.MALE, slot.getGender());
        assertEquals("skinColor", slot.getSkinColor());
        assertEquals("hairColor", slot.getHairColor());
        assertEquals("hairStyle", slot.getHairStyle());
        assertEquals("beart", slot.getBeart());
    }

    @Test
    public void testCreateFromJson () {
        JsonObject json = new JsonObject();
        json.put("cid", 10);
        json.put("name", "name");
        json.put("gender", "male");
        json.put("skinColor", "skinColor");
        json.put("hairColor", "hairColor");
        json.put("hairStyle", "hairStyle");
        json.put("beart", "beart");

        CharacterSlot slot = CharacterSlot.createFromJSON(json);

        assertEquals(10, slot.getCID());
        assertEquals("name", slot.getName());
        assertEquals(CharacterSlot.GENDER.MALE, slot.getGender());
        assertEquals("skinColor", slot.getSkinColor());
        assertEquals("hairColor", slot.getHairColor());
        assertEquals("hairStyle", slot.getHairStyle());
        assertEquals("beart", slot.getBeart());
    }

}
