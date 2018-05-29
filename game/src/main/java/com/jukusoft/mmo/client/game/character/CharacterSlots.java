package com.jukusoft.mmo.client.game.character;

import com.jukusoft.mmo.client.game.config.Config;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CharacterSlots {

    protected CharacterSlot[] myCharacterSlots = new CharacterSlot[Config.MAX_CHARACTER_SLOTS];
    protected AtomicBoolean isLoaded = new AtomicBoolean(false);

    protected CharacterSlot selectedCharacterSlot = null;

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

}
