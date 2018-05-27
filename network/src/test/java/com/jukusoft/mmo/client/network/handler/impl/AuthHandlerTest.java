package com.jukusoft.mmo.client.network.handler.impl;

import com.jukusoft.mmo.client.engine.utils.EncryptionUtils;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.login.LoginManager;
import com.jukusoft.mmo.client.network.NClient;
import com.jukusoft.mmo.client.network.Protocol;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class AuthHandlerTest {

    @Test
    public void testConstructor () {
        NClient client = Mockito.mock(NClient.class);
        new AuthHandler(client);
    }

    @Test
    public void testHandleLoginFailedResponse () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);

        AuthHandler handler = new AuthHandler(client);
        handler.loginHandler = Mockito.mock(Handler.class);
        handler.handle(createLoginFailedResponse(), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE, client, game);
    }

    @Test
    public void testHandleLoginSuccessResponse () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);

        AuthHandler handler = new AuthHandler(client);
        handler.loginHandler = Mockito.mock(Handler.class);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE, client, game);

        assertEquals(true, LoginManager.getInstance().isLoggedIn());
    }

    @Test
    public void testHandleLoginSuccessResponseWithoutRequest () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);

        AuthHandler handler = new AuthHandler(client);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE, client, game);
    }

    @Test
    public void testHandleUnknownType () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);

        AuthHandler handler = new AuthHandler(client);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_AUTH, Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_RESPONSE, client, game);
    }

    @Test
    public void testHandleUnknownType1 () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);

        AuthHandler handler = new AuthHandler(client);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_PROXY, Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_RESPONSE, client, game);
    }

    @Test
    public void testHandleUnknownType2 () {
        NClient client = Mockito.mock(NClient.class);
        WritableGame game = Mockito.mock(WritableGame.class);

        AuthHandler handler = new AuthHandler(client);
        handler.handle(createLoginSuccessResponse(), Protocol.MSG_TYPE_PROXY, Protocol.MSG_EXTENDED_TYPE_LOGIN_RESPONSE, client, game);
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
