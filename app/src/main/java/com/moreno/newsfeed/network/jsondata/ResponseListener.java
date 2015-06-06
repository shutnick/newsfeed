package com.moreno.newsfeed.network.jsondata;

/**
 * Use to react JSON data obtained and parsed.
 */
public interface ResponseListener {

    /**
     * Call when app gets response from server and handle data
     * @param title     Title of news
     * @param content   Html code, that should be parsed and dynamically placed
     * @param date      Date of news
     */
    void onResponseObtained(String title, String content, String date);
}
