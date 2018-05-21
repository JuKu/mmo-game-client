package com.jukusoft.mmo.client.game.region;

import com.jukusoft.mmo.client.game.region.Region;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegionTest {

    @Test
    public void testConstructor () {
        new Region();
    }

    @Test
    public void testGetterAndSetter () {
        Region region = new Region();

        assertEquals("", region.getTitle());
        region.setTitle("test-title");
        assertEquals("test-title", region.getTitle());
    }

}
