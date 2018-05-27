package com.jukusoft.mmo.client.network.handler.impl;

import com.jukusoft.mmo.client.engine.logging.LocalLogger;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.login.LoginManager;
import com.jukusoft.mmo.client.network.NClient;
import com.jukusoft.mmo.client.network.Protocol;
import com.jukusoft.mmo.client.network.handler.NetHandler;
import com.jukusoft.mmo.client.network.utils.MessageUtils;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public class AuthHandler implements NetHandler {

    protected Handler<LoginManager.LOGIN_RESPONSE> loginHandler = null;

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

            this.loginHandler = req.loginHandler;
            //req.loginHandler.handle(LoginManager.LOGIN_RESPONSE.WRONG_CREDENTIALS);
        });
    }

    @Override
    public void handle(Buffer content, byte type, byte extendedType, NClient client, WritableGame game) {
        if (type == Protocol.MSG_TYPE_AUTH && extendedType == Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE) {
            LocalLogger.print("login response received.");

            if (this.loginHandler == null) {
                LocalLogger.warn("received login response, but client hasnt sended login request yet.");
                return;
            }

            //get userID of logged in user
            int userID = content.getInt(Protocol.MSG_BODY_OFFSET);

            if (userID > 0) {
                //login successful
                this.loginHandler.handle(LoginManager.LOGIN_RESPONSE.SUCCESSFUL);
            } else {
                //login failed
                this.loginHandler.handle(LoginManager.LOGIN_RESPONSE.WRONG_CREDENTIALS);
            }

            this.loginHandler = null;
        }
    }

}
