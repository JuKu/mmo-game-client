package com.jukusoft.mmo.client.network.handler.impl;

import com.jukusoft.mmo.client.engine.utils.EncryptionUtils;
import com.jukusoft.mmo.client.game.Game;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.character.CharacterSlot;
import com.jukusoft.mmo.client.game.character.CharacterSlots;
import com.jukusoft.mmo.client.game.config.Config;
import com.jukusoft.mmo.client.game.login.LoginManager;
import com.jukusoft.mmo.client.network.NClient;
import com.jukusoft.mmo.client.network.Protocol;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AuthHandlerTest {

    protected boolean exected = false;

    @Test
    public void testConstructor () {
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());
        NClient client = Mockito.mock(NClient.class);
        new AuthHandler(client, game);
    }

    @Test
    public void testConstructor1 () {
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());
        NClient client = new NClient(Mockito.mock(WritableGame.class)) {

            @Override
            public void send(Buffer content) {
                throw new RuntimeException("test exception");
            }
        };
        new AuthHandler(client, game);

        //try to login
        LoginManager.getInstance().login("user", "password", Mockito.mock(Handler.class));
    }

    @Test
    public void testExecuteLogin () throws NoSuchAlgorithmException {
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());
        NClient client = new NClient(Mockito.mock(WritableGame.class)) {

            @Override
            public void send(Buffer content) {
                throw new RuntimeException("test exception");
            }
        };

        EncryptionUtils.init(EncryptionUtils.generateKeyPair().getPublic());

        AuthHandler handler = new AuthHandler(client, game);
        handler.executeLogin(client, new LoginManager.LoginRequest("user", "password", Mockito.mock(Handler.class)));
    }

    @Test
    public void testExecuteLogin1 () throws NoSuchAlgorithmException {
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());
        NClient client = Mockito.mock(NClient.class);

        EncryptionUtils.init(EncryptionUtils.generateKeyPair().getPublic());

        AuthHandler handler = new AuthHandler(client, game);
        handler.executeLogin(client, new LoginManager.LoginRequest("user", "password", Mockito.mock(Handler.class)));
    }

    @Test
    public void testHandleLoginFailedResponse () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.loginHandler = Mockito.mock(Handler.class);
        handler.handle(createLoginFailedResponse(), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE, client, game);
    }

    @Test
    public void testHandleLoginSuccessResponse () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.loginHandler = Mockito.mock(Handler.class);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE, client, game);

        assertEquals(true, LoginManager.getInstance().isLoggedIn());
    }

    @Test
    public void testHandleLoginSuccessResponseWithoutRequest () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE, client, game);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testHandleUnknownExtendedType () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_RESPONSE, client, game);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testHandleUnknownType () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_PROXY, Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_RESPONSE, client, game);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testHandleUnknownType1 () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_PROXY, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE, client, game);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testHandleReceiveTooMuchCharacterSlots () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.handle(createCharacterSlotsResponse(Config.MAX_CHARACTER_SLOTS + 1), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_LIST_CHARACTERS_RESPONSE, client, game);
    }

    @Test
    public void testHandleReceiveOneCharacterSlot () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        CharacterSlots slots = new CharacterSlots();
        Mockito.when(game.getCharacterSlots()).thenReturn(slots);

        assertEquals(false, slots.isLoaded());
        assertEquals(0, slots.countSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.handle(createCharacterSlotsResponse(1), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_LIST_CHARACTERS_RESPONSE, client, game);

        assertEquals(true, slots.isLoaded());
        assertEquals(1, slots.countSlots());

        CharacterSlot[] slotArray = slots.getSlots();
        assertNotNull(slotArray[0]);

        for (int i = 1; i < Config.MAX_CHARACTER_SLOTS; i++) {
            assertNull(slotArray[i]);
        }
    }

    @Test
    public void testHandleReceiveCharacterSlots () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        CharacterSlots slots = new CharacterSlots();
        Mockito.when(game.getCharacterSlots()).thenReturn(slots);

        assertEquals(false, slots.isLoaded());
        assertEquals(0, slots.countSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.handle(createCharacterSlotsResponse(Config.MAX_CHARACTER_SLOTS), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_LIST_CHARACTERS_RESPONSE, client, game);

        assertEquals(true, slots.isLoaded());
        assertEquals(Config.MAX_CHARACTER_SLOTS, slots.countSlots());

        CharacterSlot[] slotArray = slots.getSlots();

        for (int i = 0; i < Config.MAX_CHARACTER_SLOTS; i++) {
            assertNotNull(slotArray[i]);
        }
    }

    @Test (expected = IllegalStateException.class)
    public void testHandleReceiveCreateCharacterResponseWithoutCreateHandler () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        CharacterSlots slots = new CharacterSlots();
        Mockito.when(game.getCharacterSlots()).thenReturn(slots);

        assertEquals(false, slots.isLoaded());
        assertEquals(0, slots.countSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.handle(createCharacterResponse(1), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_CREATE_CHARACTER_RESPONSE, client, game);
    }

    @Test
    public void testHandleReceiveCreateCharacterResponse () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);
        CharacterSlots slots = new CharacterSlots();
        Mockito.when(game.getCharacterSlots()).thenReturn(slots);

        assertEquals(false, slots.isLoaded());
        assertEquals(0, slots.countSlots());

        AuthHandler handler = new AuthHandler(client, game);
        handler.createCharacterHandler = new Handler<CharacterSlots.CREATE_CHARACTER_RESULT>() {
            @Override
            public void handle(CharacterSlots.CREATE_CHARACTER_RESULT res) {
                exected = true;
            }
        };

        assertEquals(false, this.exected);

        handler.handle(createCharacterResponse(1), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_CREATE_CHARACTER_RESPONSE, client, game);
        assertEquals(true, this.exected);
        this.exected = false;

        handler.handle(createCharacterResponse(2), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_CREATE_CHARACTER_RESPONSE, client, game);
        assertEquals(true, this.exected);
        this.exected = false;

        handler.handle(createCharacterResponse(3), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_CREATE_CHARACTER_RESPONSE, client, game);
        assertEquals(true, this.exected);
        this.exected = false;

        handler.handle(createCharacterResponse(4), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_CREATE_CHARACTER_RESPONSE, client, game);
        assertEquals(true, this.exected);
        this.exected = false;

        handler.handle(createCharacterResponse(5), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_CREATE_CHARACTER_RESPONSE, client, game);
        assertEquals(true, this.exected);
        this.exected = false;
    }

    @Test
    public void testExecuteCreateCharacter () {
        NClient client = Mockito.mock(NClient.class);
        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());
        AuthHandler handler = new AuthHandler(client, game);

        CharacterSlot slot = Mockito.mock(CharacterSlot.class);
        Mockito.when(slot.toJson()).thenReturn(new JsonObject());

        CharacterSlots.CreateCharacterRequest req = new CharacterSlots.CreateCharacterRequest(slot, Mockito.mock(Handler.class));
        handler.executeCreateCharacter(client, req);
    }

    @Test
    public void testExecuteCreateCharacter1 () {
        NClient client = Mockito.mock(NClient.class);
        Mockito.doThrow(Exception.class).when(client).send(Mockito.isA(Buffer.class));
        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getCharacterSlots()).thenReturn(new CharacterSlots());
        AuthHandler handler = new AuthHandler(client, game);

        CharacterSlots.CreateCharacterRequest req = new CharacterSlots.CreateCharacterRequest(Mockito.mock(CharacterSlot.class), Mockito.mock(Handler.class));
        handler.executeCreateCharacter(client, req);
    }

    protected Buffer createCharacterSlotsResponse (int length) {
        Buffer content = Buffer.buffer();

        //set header
        content.setByte(0, Protocol.MSG_TYPE_AUTH);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_LIST_CHARACTERS_RESPONSE);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();

        for (int i = 0; i < length; i++) {
            //create character

            JsonObject json1 = new JsonObject();
            json1.put("cid", 10 + i);
            json1.put("name", "name");
            json1.put("gender", "male");
            json1.put("skinColor", "skinColor");
            json1.put("hairColor", "hairColor");
            json1.put("hairStyle", "hairStyle");
            json1.put("beart", "beart");

            array.add(json1);
        }

        json.put("slots", array);
        String jsonStr = json.encode();

        content.setInt(Protocol.MSG_BODY_OFFSET, jsonStr.length());
        content.setString(Protocol.MSG_BODY_OFFSET + 4, jsonStr);

        return content;
    }

    protected Buffer createCharacterResponse (int resultCode) {
        Buffer content = Buffer.buffer();

        //set header
        content.setByte(0, Protocol.MSG_TYPE_AUTH);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_CREATE_CHARACTER_RESPONSE);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        content.setInt(Protocol.MSG_BODY_OFFSET, resultCode);

        return content;
    }

    protected Buffer createLoginFailedResponse () {
        Buffer content = Buffer.buffer();

        content.setByte(0, Protocol.MSG_TYPE_AUTH);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        content.setInt(Protocol.MSG_BODY_OFFSET, 0);

        return content;
    }

    protected Buffer createLoginSuccessResponse () {
        Buffer content = Buffer.buffer();

        content.setByte(0, Protocol.MSG_TYPE_AUTH);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        content.setInt(Protocol.MSG_BODY_OFFSET, 1);

        return content;
    }

}
