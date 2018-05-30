package com.jukusoft.mmo.client.network.utils;

import com.jukusoft.mmo.client.engine.utils.EncryptionUtils;
import com.jukusoft.mmo.client.game.character.CharacterSlot;
import com.jukusoft.mmo.client.network.Protocol;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

public class MessageUtils {

    protected MessageUtils() {
        //
    }

    public static Buffer createMsg (byte type, byte extendedType, int cid) {
        Buffer content = Buffer.buffer();

        content.setByte(0, type);
        content.setByte(1, extendedType);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, cid);

        return content;
    }

    public static Buffer createRTTMsg () {
        Buffer content = Buffer.buffer();

        content.setByte(0, Protocol.MSG_TYPE_PROXY);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_RTT);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        return content;
    }

    public static Buffer createPublicKeyRequest () {
        Buffer content = Buffer.buffer();

        content.setByte(0, Protocol.MSG_TYPE_PROXY);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_REQUEST);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        return content;
    }

    public static Buffer createLoginRequest (String username, String password) throws Exception {
        Buffer content = Buffer.buffer();

        content.setByte(0, Protocol.MSG_TYPE_AUTH);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_LOGIN_REQUEST);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        JsonObject json = new JsonObject();
        json.put("username", username);
        json.put("password", password);

        //encrypt message
        byte[] encrypted = EncryptionUtils.encrypt(json.encode());

        //set length
        content.setInt(Protocol.MSG_BODY_OFFSET, encrypted.length);
        content.setBytes(Protocol.MSG_BODY_OFFSET + 4, encrypted);

        return content;
    }

    public static Buffer createCharacterListRequest () {
        Buffer content = Buffer.buffer();

        content.setByte(0, Protocol.MSG_TYPE_AUTH);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_LIST_CHARACTERS_REQUEST);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        return content;
    }

    public static Buffer createCharacterRequest (CharacterSlot character) {
        Buffer content = Buffer.buffer();

        content.setByte(0, Protocol.MSG_TYPE_AUTH);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_CREATE_CHARACTER_REQUEST);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        String jsonStr = character.toJson().encode();

        content.setInt(Protocol.MSG_BODY_OFFSET, jsonStr.length());
        content.setString(Protocol.MSG_BODY_OFFSET + 4, jsonStr);

        return content;
    }

    public static Buffer createErrorMsg (byte extendedType, int cid) {
        Buffer content = Buffer.buffer();

        content.setByte(0, Protocol.MSG_TYPE_ERROR);
        content.setByte(1, extendedType);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, cid);

        return content;
    }

}
