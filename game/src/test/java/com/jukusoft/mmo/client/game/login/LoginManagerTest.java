package com.jukusoft.mmo.client.game.login;

import io.vertx.core.Handler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginManagerTest {

    @Test
    public void testConstructor () {
        new LoginManager();
    }

    @Test
    public void testGetInstance () {
        LoginManager instance = LoginManager.getInstance();
        assertNotNull(instance);

        //check, if instances are equals
        LoginManager instance1 = LoginManager.getInstance();
        assertEquals(instance, instance1);
    }

    @Test
    public void testIsLoggedIn () {
        LoginManager instance = LoginManager.getInstance();
        instance.loggedIn.set(false);
        assertEquals(false, instance.isLoggedIn());

        instance.loggedIn.set(true);
        assertEquals(true, instance.isLoggedIn());
    }

    @Test (expected = IllegalStateException.class)
    public void testLoginWithoutExecutor () {
        LoginManager.getInstance().loginExecutor = null;

        LoginManager.getInstance().login("user", "password", new Handler<LoginManager.LOGIN_RESPONSE>() {
            @Override
            public void handle(LoginManager.LOGIN_RESPONSE event) {
                //
            }
        });
    }

    @Test (expected = NullPointerException.class)
    public void testNullUser () {
        LoginManager.getInstance().loginExecutor = new Handler<LoginManager.LoginRequest>() {
            @Override
            public void handle(LoginManager.LoginRequest event) {
                //
            }
        };

        LoginManager.getInstance().login(null, "password", createHandler());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptyUser () {
        LoginManager.getInstance().loginExecutor = new Handler<LoginManager.LoginRequest>() {
            @Override
            public void handle(LoginManager.LoginRequest event) {
                //
            }
        };

        LoginManager.getInstance().login("", "password", createHandler());
    }

    @Test (expected = NullPointerException.class)
    public void testNullPassword () {
        LoginManager.getInstance().loginExecutor = new Handler<LoginManager.LoginRequest>() {
            @Override
            public void handle(LoginManager.LoginRequest event) {
                //
            }
        };

        LoginManager.getInstance().login("user", null, createHandler());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptyPassword () {
        LoginManager.getInstance().loginExecutor = new Handler<LoginManager.LoginRequest>() {
            @Override
            public void handle(LoginManager.LoginRequest event) {
                //
            }
        };

        LoginManager.getInstance().login("user", "", createHandler());
    }

    @Test (expected = NullPointerException.class)
    public void testNullHandler () {
        LoginManager.getInstance().loginExecutor = new Handler<LoginManager.LoginRequest>() {
            @Override
            public void handle(LoginManager.LoginRequest event) {
                //
            }
        };

        LoginManager.getInstance().login("user", "password", null);
    }

    protected Handler<LoginManager.LOGIN_RESPONSE> createHandler () {
        return new Handler<LoginManager.LOGIN_RESPONSE>() {
            @Override
            public void handle(LoginManager.LOGIN_RESPONSE event) {
                //
            }
        };
    }

}
