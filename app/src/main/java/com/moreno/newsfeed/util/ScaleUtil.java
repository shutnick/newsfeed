package com.moreno.newsfeed.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Scale util
 */
public class ScaleUtil {

    /**
     * Get scale factor relatively to screen width
     */
    public static int getScale(Context context, int elementWidth) {
        Display display = ((WindowManager) context.
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenWidth = display.getWidth();
        double koef = screenWidth * 1.D / elementWidth;
        return (int) (koef * 100);
    }
}
