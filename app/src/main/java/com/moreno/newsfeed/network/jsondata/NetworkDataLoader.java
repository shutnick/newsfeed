package com.moreno.newsfeed.network.jsondata;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.moreno.newsfeed.R;
import com.moreno.newsfeed.ui.warning.WarningDialog;
import com.moreno.newsfeed.util.CheckInternetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class manages loading JSON data and it's handling
 * Created by Moreno on 27.05.2015.
 */
public class NetworkDataLoader {
    public static final String NEWS_TITLE = "title";
    public static final String NEWS_CONTENT = "content";
    public static final String NEWS_DATE = "created";
    public static final String URL_WITHOUT_PROTOCOL = "\"//";
    public static final String URL_WITH_HTTP_PROTOCOL = "\"http://";
    public static final String LAGOS_TIMEZONE = "Africa/Lagos";
    public final String TAG = getClass().getSimpleName();
    private Activity mActivity;

    public NetworkDataLoader(Activity activity) {
        mActivity = activity;
    }

    /**
     * Call to get data from server
     * @param url   URL of request
     */
    public void load (String url) {
        if (!CheckInternetUtil.isInternetEnabled(mActivity)) {
            WarningDialog.createDialog(R.string.warning_dialog_title_internet
                    , R.string.warning_dialog_message_no_internet)
                    .show(mActivity.getFragmentManager(), WarningDialog.TAG);
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    handleJSON(response);
                } catch (JSONException e) {
                    WarningDialog.createDialog(R.string.warning_dialog_error_title
                            , R.string.warning_dialog_error_parsing_json_message)
                    .show(mActivity.getFragmentManager(), WarningDialog.TAG);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                WarningDialog.createDialog(R.string.warning_dialog_connection_error_title
                        , R.string.warning_dialog_connection_error_message)
                .show(mActivity.getFragmentManager(), WarningDialog.TAG);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS
                , DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    private void handleJSON(JSONObject response) throws JSONException {
        String title = response.getString(NEWS_TITLE);
        String content = response.getString(NEWS_CONTENT);
        long date = Long.parseLong(response.getString(NEWS_DATE));

        if (content.contains(URL_WITHOUT_PROTOCOL)) {
            content = content.replaceAll(URL_WITHOUT_PROTOCOL, URL_WITH_HTTP_PROTOCOL);
        }

        String formattedDate = formatDate(date);
        ((ResponseListener) mActivity).onResponseObtained(title, content, formattedDate);
    }

    private String formatDate(long date) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.setTimeZone(TimeZone.getTimeZone(LAGOS_TIMEZONE));
        return format.format(new Date(date));
    }
}
