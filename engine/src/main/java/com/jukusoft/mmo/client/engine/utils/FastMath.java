package com.jukusoft.mmo.client.engine.utils;

/**
 * Created by Justin on 11.02.2017.
 */
public class FastMath {

    public static final float PI = 3.1415926535897932384626433832795f;
    public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

    protected FastMath () {
        //
    }

    /**
    * convert degree to "bogenmaß" radians
    */
    public static final float toRadians (float angleGrad) {
        while (angleGrad < 0) {
            angleGrad += 360;
        }

        angleGrad = angleGrad % 360;
        return FastMath.PI / 180 * angleGrad;
    }

}
