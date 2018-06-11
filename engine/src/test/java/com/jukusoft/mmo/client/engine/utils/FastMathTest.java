package com.jukusoft.mmo.client.engine.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FastMathTest {

    @Test
    public void testConstructor () {
        new FastMath();
    }

    @Test
    public void testToRadians (){
        assertEquals(0, FastMath.toRadians(0), 0.0001f);

        assertEquals(1.5707964, FastMath.toRadians(90), 0.0001f);//90 degree
        assertEquals(3.14159, FastMath.toRadians(180), 0.0001f);//180 degree
        assertEquals(4.71239, FastMath.toRadians(270), 0.0001f);//270 degree
        assertEquals(0, FastMath.toRadians(360), 0.0001f);//360 degree

        assertEquals(1.5707964, FastMath.toRadians(450), 0.0001f);//450 degree = 90 degree
        assertEquals(3.14159, FastMath.toRadians(540), 0.0001f);//90 degree = 180 degree
    }

}
