package com.jukusoft.mmo.client.game.login;

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

}
