package com.jukusoft.mmo.client.network.handler.impl;

import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.login.LoginManager;
import com.jukusoft.mmo.client.network.NClient;
import com.jukusoft.mmo.client.network.handler.NetHandler;
import com.jukusoft.mmo.client.network.utils.MessageUtils;
import io.vertx.core.buffer.Buffer;

public class AuthHandler implements NetHandler {

    public AuthHandler(NClient client) {
        //register login executor
        LoginManager.getInstance().setLoginExecutor((LoginManager.LoginRequest req) -> {
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

            req.loginHandler.handle(LoginManager.LOGIN_RESPONSE.WRONG_CREDENTIALS);
        });
    }

    @Override
    public void handle(Buffer content, byte type, byte extendedType, NClient client, WritableGame game) {

    }

}
