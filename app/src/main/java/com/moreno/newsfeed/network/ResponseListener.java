package com.moreno.newsfeed.network;

/**
 * Created by Moreno on 27.05.2015.
 */
public interface ResponseListener {

    /**
     * Call when app gets response from server and handle data
     * @param scale     Scale factor to fit content to screen
     * @param title     Title of news
     * @param content   Html code, that should be set to WebView
     * @param date      Date of news
     */
    void onResponseObtained(int scale, String title, String content, String date);
}
