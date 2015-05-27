package com.moreno.newsfeed.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.moreno.newsfeed.R;
import com.moreno.newsfeed.network.NetworkDataLoader;
import com.moreno.newsfeed.network.ResponseListener;
import com.moreno.newsfeed.ui.warning.WarningDialog;

public class NewsFeedActivity extends AppCompatActivity implements ResponseListener{
    public static final String DEFAULT_URL = "http://api.naij.com/test.json";
    public static final String HTML_MIME_TYPE = "text/html";
    public static final String UTF_8_ENCODING = "UTF-8";
    public final String TAG = getClass().getSimpleName();
    private TextView mTitleView;
    private WebView mContentView;
    private TextView mDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        mTitleView = (TextView) findViewById(R.id.title_view);
        mContentView = (WebView) findViewById(R.id.content_view);
        prepareWebView();
        mDateView = (TextView) findViewById(R.id.date_view);
    }

    private void prepareWebView() {
        WebSettings contentWebSettings = mContentView.getSettings();
        contentWebSettings.setJavaScriptEnabled(true);
        contentWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        contentWebSettings.setDomStorageEnabled(true);
        String cachePath = getCacheDir().getAbsolutePath();
        contentWebSettings.setAppCachePath(cachePath);
        contentWebSettings.setAllowFileAccess(true);
        contentWebSettings.setAppCacheEnabled(true);
        contentWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        contentWebSettings.setDatabaseEnabled(true);
        mContentView.setWebChromeClient(new WebChromeClient());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.load_news) {
            loadNews();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponseObtained(int scale, String title, String content, String date) {
        mTitleView.setText(title);
        mContentView.setInitialScale(scale);
        mContentView.loadData(content, HTML_MIME_TYPE, UTF_8_ENCODING);
        mDateView.setText(date);

        Log.i(TAG, content);
    }

    public void loadNews() {
        new NetworkDataLoader(this).load(DEFAULT_URL);
    }


}
