package com.jukusoft.mmo.client.network.handler.impl;

import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.engine.utils.ByteUtils;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.character.CharacterSlot;
import com.jukusoft.mmo.client.game.character.CharacterSlots;
import com.jukusoft.mmo.client.game.login.LoginManager;
import com.jukusoft.mmo.client.network.NClient;
import com.jukusoft.mmo.client.network.Protocol;
import com.jukusoft.mmo.client.network.handler.NetHandler;
import com.jukusoft.mmo.client.network.utils.MessageUtils;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class AuthHandler implements NetHandler {

    protected Handler<LoginManager.LOGIN_RESPONSE> loginHandler = null;

    public AuthHandler(NClient client) {
        //register login executor
        LoginManager.getInstance().setLoginExecutor((LoginManager.LoginRequest req) -> this.executeLogin(client, req));
    }

    protected void executeLogin (NClient client, LoginManager.LoginRequest req) {
        LocalLogger.print("try to login user");

        //send login message
        try {
            LocalLogger.print("send login request...");

            Buffer msg = MessageUtils.createLoginRequest(req.user, req.password);
            client.send(msg);
        } catch (Exception e) {
            //internal client error, e.q. with encryption
            LocalLogger.printStacktrace(e);
            req.loginHandler.handle(LoginManager.LOGIN_RESPONSE.CLIENT_ERROR);
        }

        this.loginHandler = req.loginHandler;
    }

    @Override
    public void handle(Buffer content, byte type, byte extendedType, NClient client, WritableGame game) {
        if (type == Protocol.MSG_TYPE_AUTH) {
            if (extendedType == Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE) {
                LocalLogger.print("login response received.");

                if (this.loginHandler == null) {
                    LocalLogger.warn("received login response, but client hasnt sended login request yet.");
                    return;
                }

                //get userID of logged in user
                int userID = content.getInt(Protocol.MSG_BODY_OFFSET);

                if (userID > 0) {
                    //login successful

                    //set user as logged in
                    LoginManager.getInstance().setLoggedIn(true);

                    //send character list request to proxy
                    Buffer msg = MessageUtils.createCharacterListRequest();
                    client.send(msg);

                    this.loginHandler.handle(LoginManager.LOGIN_RESPONSE.SUCCESSFUL);
                } else {
                    //login failed
                    this.loginHandler.handle(LoginManager.LOGIN_RESPONSE.WRONG_CREDENTIALS);
                }

                this.loginHandler = null;
            } else if (extendedType == Protocol.MSG_EXTENDED_TYPE_LIST_CHARACTERS_RESPONSE) {
                LocalLogger.print("list of character slots received.");

                //get length of string
                int length = content.getInt(Protocol.MSG_BODY_OFFSET);

                //get json string
                String jsonStr = content.getString(Protocol.MSG_BODY_OFFSET + 4, Protocol.MSG_BODY_OFFSET + 4 + length);

                //convert string to json object
                JsonObject json = new JsonObject(jsonStr);
                JsonArray array = json.getJsonArray("slots");

                //get character slot manager
                CharacterSlots characterSlots = game.getCharacterSlots();

                //list with slots
                List<CharacterSlot> list = new ArrayList<>();

                for (int i = 0; i < array.size(); i++) {
                    JsonObject json1 = array.getJsonObject(i);

                    //create slot and add to list
                    CharacterSlot slot = CharacterSlot.createFromJSON(json1);
                    list.add(slot);
                }

                //load slots
                characterSlots.load(list);
            }
        } else {
            throw new IllegalArgumentException("type 0x" + ByteUtils.byteToHex(type) + " isnt supported by AuthHandler.");
        }
    }

}
