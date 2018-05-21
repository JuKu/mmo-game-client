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
        WritableRegion region = new WritableRegion();

        assertEquals("", region.getTitle());
        region.setTitle("test-title");
        assertEquals("test-title", region.getTitle());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testJoinNullRegionID () {
        WritableRegion region = new WritableRegion();
        region.join(0, 1, "test");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testJoinNullInstanceID () {
        WritableRegion region = new WritableRegion();
        region.join(1, 0, "test");
    }

    @Test (expected = NullPointerException.class)
    public void testJoinNullTitle () {
        WritableRegion region = new WritableRegion();
        region.join(1, 1, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testJoinEmptyTitle () {
        WritableRegion region = new WritableRegion();
        region.join(1, 1, "");
    }

}
