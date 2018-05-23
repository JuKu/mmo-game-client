package com.jukusoft.mmo.client.engine.utils;

import com.jukusoft.mmo.client.engine.logging.LocalLogger;

public class Utils {

    protected Utils() {
        //
    }

    public static String getSection (final String section) {
        if (section == null) {
            throw new NullPointerException("section cannot be null.");
        }

        if (section.isEmpty()) {
            throw new IllegalArgumentException("section cannot be empty.");
        }

        StringBuilder sb = new StringBuilder();

        sb.append("===={ " + section + " }");

        while (sb.length() < 80) {
            sb.append("-");
        }

        return sb.toString();
    }

    public static void printSection (final String section) {
        String s = getSection(section);
        LocalLogger.print("\n" + s);
    }

}
