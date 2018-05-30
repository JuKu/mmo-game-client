package com.jukusoft.mmo.client.game.character;

import com.jukusoft.mmo.client.game.config.Config;
import io.vertx.core.Handler;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CharacterSlots {

    protected CharacterSlot[] myCharacterSlots = new CharacterSlot[Config.MAX_CHARACTER_SLOTS];
    protected AtomicBoolean isLoaded = new AtomicBoolean(false);

    protected CharacterSlot selectedCharacterSlot = null;

    public enum CREATE_CHARACTER_RESULT {
        DUPLICATE_NAME, ERROR, SUCCESS
    }

    public boolean isLoaded () {
        return isLoaded.get();
    }

    public void load (List<CharacterSlot> list) {
        if (list.size() > Config.MAX_CHARACTER_SLOTS) {
            throw new IllegalArgumentException("maximal " + Config.MAX_CHARACTER_SLOTS + " characters are allowed!");
        }

        int i = 0;

        for (CharacterSlot slot : list) {
            this.myCharacterSlots[i] = slot;

            i++;
        }

        isLoaded.set(true);
    }

    public CharacterSlot[] getSlots () {
        return this.myCharacterSlots;
    }

    public int countSlots () {
        int count = 0;

        for (int i = 0; i < this.myCharacterSlots.length; i++) {
            if (this.myCharacterSlots[i] != null) {
                count++;
            }
        }

        return count;
    }

    public CharacterSlot getSelectedCharacterSlot() {
        return this.selectedCharacterSlot;
    }

    public void createCharacter (CharacterSlot character, Handler<CREATE_CHARACTER_RESULT> handler) {
        if (character == null) {
            throw new NullPointerException("character cannot be null.");
        }

        if (character.getCID() != Integer.MAX_VALUE) {
            throw new IllegalArgumentException("character wasnt created from client.");
        }

        handler.handle(CREATE_CHARACTER_RESULT.DUPLICATE_NAME);
    }

}
