package com.moreno.newsfeed.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Check internet state
 */
public class CheckInternetUtil {

    /**
     * Check if internet connection available
     * @param context    Context
     * @return  True if device connected to Internet
     */
    public static boolean isInternetEnabled(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
