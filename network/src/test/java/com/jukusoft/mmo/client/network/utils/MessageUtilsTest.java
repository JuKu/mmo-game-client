package com.jukusoft.mmo.client.network.utils;

import com.jukusoft.mmo.client.engine.utils.EncryptionUtils;
import com.jukusoft.mmo.client.game.character.CharacterSlot;
import com.jukusoft.mmo.client.network.Protocol;
import io.vertx.core.buffer.Buffer;
import org.junit.Test;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageUtilsTest {

    @Test
    public void testConstructor () {
        new MessageUtils();
    }

    @Test
    public void testCreateMessage () {
        Buffer content = MessageUtils.createMsg(Protocol.MSG_TYPE_PROXY, Protocol.MSG_EXTENDED_TYPE_RTT, 10);

        //check header
        assertEquals(Protocol.MSG_TYPE_PROXY, content.getByte(0));
        assertEquals(Protocol.MSG_EXTENDED_TYPE_RTT, content.getByte(1));
        assertEquals(Protocol.MSG_PROTOCOL_VERSION, content.getShort(2));
        assertEquals(10, content.getInt(4));
    }

    @Test
    public void testCreateRTTMessage () {
        Buffer content = MessageUtils.createRTTMsg();

        //check header
        assertEquals(Protocol.MSG_TYPE_PROXY, content.getByte(0));
        assertEquals(Protocol.MSG_EXTENDED_TYPE_RTT, content.getByte(1));
        assertEquals(Protocol.MSG_PROTOCOL_VERSION, content.getShort(2));
        assertEquals(0, content.getInt(4));
    }

    @Test
    public void testCreatePublicKeyRequest () {
        Buffer content = MessageUtils.createPublicKeyRequest();

        //check header
        assertEquals(Protocol.MSG_TYPE_PROXY, content.getByte(0));
        assertEquals(Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_REQUEST, content.getByte(1));
        assertEquals(Protocol.MSG_PROTOCOL_VERSION, content.getShort(2));
        assertEquals(0, content.getInt(4));
    }

    @Test
    public void testCreateLoginRequest () throws Exception {
        //generate test key pair
        KeyPair keyPair = EncryptionUtils.generateKeyPair();
        EncryptionUtils.init(keyPair.getPublic());

        Buffer content = MessageUtils.createLoginRequest("username", "password");

        //check header
        assertEquals(Protocol.MSG_TYPE_AUTH, content.getByte(0));
        assertEquals(Protocol.MSG_EXTENDED_TYPE_LOGIN_REQUEST, content.getByte(1));
        assertEquals(Protocol.MSG_PROTOCOL_VERSION, content.getShort(2));
        assertEquals(0, content.getInt(4));

        int length = content.getInt(Protocol.MSG_BODY_OFFSET);
        assertEquals(true, length > 0);

        byte[] encrypted = content.getBytes(Protocol.MSG_BODY_OFFSET + 4, Protocol.MSG_BODY_OFFSET + 4 + length);
        assertEquals(true, encrypted.length > 0);
    }

    @Test
    public void testCreateCharacterListRequest () {
        Buffer content = MessageUtils.createCharacterListRequest();

        //check header
        assertEquals(Protocol.MSG_TYPE_AUTH, content.getByte(0));
        assertEquals(Protocol.MSG_EXTENDED_TYPE_LIST_CHARACTERS_REQUEST, content.getByte(1));
        assertEquals(Protocol.MSG_PROTOCOL_VERSION, content.getShort(2));
        assertEquals(0, content.getInt(4));
    }

    @Test
    public void testCreateCharacterRequest () {
        Buffer content = MessageUtils.createCharacterRequest(CharacterSlot.create("name", CharacterSlot.GENDER.MALE, "skinColor", "hairColor", "hairStyle", "beart"));

        //check header
        assertEquals(Protocol.MSG_TYPE_AUTH, content.getByte(0));
        assertEquals(Protocol.MSG_EXTENDED_TYPE_CREATE_CHARACTER_REQUEST, content.getByte(1));
        assertEquals(Protocol.MSG_PROTOCOL_VERSION, content.getShort(2));
        assertEquals(0, content.getInt(4));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCreateSelectCharacterNullCIDRequest () {
        Buffer content = MessageUtils.createSelectCharacterRequest(0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCreateSelectIntegerMaxValueCharacterRequest () {
        Buffer content = MessageUtils.createSelectCharacterRequest(Integer.MAX_VALUE);
    }

    @Test
    public void testCreateSelectCharacterRequest () {
        Buffer content = MessageUtils.createSelectCharacterRequest(10);

        //check header
        assertEquals(Protocol.MSG_TYPE_AUTH, content.getByte(0));
        assertEquals(Protocol.MSG_EXTENDED_TYPE_SELECT_CHARACTER_REQUEST, content.getByte(1));
        assertEquals(Protocol.MSG_PROTOCOL_VERSION, content.getShort(2));
        assertEquals(0, content.getInt(4));

        //check cid
        assertEquals(10, content.getInt(Protocol.MSG_BODY_OFFSET));
    }

    @Test
    public void testCreateErrorMsg () {
        Buffer content = MessageUtils.createErrorMsg(Protocol.MSG_EXTENDED_TYPE_INTERNAL_SERVER_ERROR, 200);

        //check header
        assertEquals(Protocol.MSG_TYPE_ERROR, content.getByte(0));
        assertEquals(Protocol.MSG_EXTENDED_TYPE_INTERNAL_SERVER_ERROR, content.getByte(1));
        assertEquals(Protocol.MSG_PROTOCOL_VERSION, content.getShort(2));
        assertEquals(200, content.getInt(4));
    }

}
