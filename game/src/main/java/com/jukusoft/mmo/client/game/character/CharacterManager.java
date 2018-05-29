package com.jukusoft.mmo.client.game.character;

import com.jukusoft.mmo.client.game.config.Config;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CharacterManager {

    protected Character[] myCharacters = new Character[Config.MAX_CHARACTER_SLOTS];
    protected AtomicBoolean isLoaded = new AtomicBoolean(false);

    protected Character selectedCharacter = null;

    public boolean isLoaded () {
        return isLoaded.get();
    }

    public void load (List<Character> list) {
        if (list.size() > Config.MAX_CHARACTER_SLOTS) {
            throw new IllegalArgumentException("maximal " + Config.MAX_CHARACTER_SLOTS + " characters are allowed!");
        }

        isLoaded.set(true);
    }

    public Character getSelectedCharacter () {
        return this.selectedCharacter;
    }

}
