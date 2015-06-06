package com.moreno.newsfeed.util;

import android.util.Log;

/**
 * Logging control util
 */
public class Logger {
    private static boolean sEnabled = true;
    public static void info(String tag, String message) {
        if (sEnabled) {
            Log.i(tag, message);
        }
    }

    public static void error(String tag, String message) {
        if (sEnabled) {
            Log.e(tag, message);
        }
    }

    public static void debug(String tag, String message) {
        if (sEnabled) {
            Log.d(tag, message);
        }
    }

}
