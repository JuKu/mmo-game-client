package com.jukusoft.mmo.client.game.character;

import io.vertx.core.Handler;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class CharacterSlotsTest {

    protected boolean executed = false;

    @Test
    public void testConstructor () {
        CharacterSlots slots = new CharacterSlots();
        assertEquals(false, slots.isLoaded());
    }

    @Test
    public void testReset () {
        CharacterSlots slots = new CharacterSlots();
        slots.isLoaded.set(true);

        slots.reset();
        assertEquals(false, slots.isLoaded());
    }

    @Test
    public void testEnum () {
        CharacterSlots.CREATE_CHARACTER_RESULT res = CharacterSlots.CREATE_CHARACTER_RESULT.SUCCESS;
    }

    @Test (expected = IllegalStateException.class)
    public void testCreateCharacterWithoutExecutor () {
        CharacterSlots slots = new CharacterSlots();
        slots.createCharacter(null, Mockito.mock(Handler.class));
    }

    @Test (expected = NullPointerException.class)
    public void testCreateNullCharacter () {
        CharacterSlots slots = new CharacterSlots();
        slots.setCreateCharacterExecutor(Mockito.mock(Handler.class));
        slots.createCharacter(null, Mockito.mock(Handler.class));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCreateCharacterWithWrongCID () {
        CharacterSlots slots = new CharacterSlots();
        slots.setCreateCharacterExecutor(Mockito.mock(Handler.class));

        CharacterSlot character = new CharacterSlot(0, "name", CharacterSlot.GENDER.MALE, "skinColor", "hairColor", "hairStyle", "beart");
        slots.createCharacter(character, Mockito.mock(Handler.class));
    }

    @Test
    public void testCreateCharacter () {
        this.executed = false;

        CharacterSlots slots = new CharacterSlots();
        slots.setCreateCharacterExecutor(req -> {
            this.executed = true;
        });

        CharacterSlot character = CharacterSlot.create("name", CharacterSlot.GENDER.MALE, "skinColor", "hairColor", "hairStyle", "beart");
        slots.createCharacter(character, Mockito.mock(Handler.class));

        assertEquals(true, this.executed);
    }

}
