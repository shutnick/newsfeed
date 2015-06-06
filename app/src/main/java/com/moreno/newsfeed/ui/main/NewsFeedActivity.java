package com.moreno.newsfeed.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moreno.newsfeed.R;
import com.moreno.newsfeed.network.jsondata.NetworkDataLoader;
import com.moreno.newsfeed.network.jsondata.ResponseListener;
import com.moreno.newsfeed.ui.main.content.ContentMaker;

/**
 * Main activity, that shows news
 */
public class NewsFeedActivity extends AppCompatActivity implements ResponseListener{
    public static final String DEFAULT_URL = "http://api.naij.com/test.json";
    public static final String NEWS_ALREADY_SHOWN = "news already shown";
    public final String TAG = getClass().getSimpleName();
    private TextView mTitleView;
    private TextView mDateView;
    private LinearLayout mContentView;
    private boolean mNewsLoadingStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        mTitleView = (TextView) findViewById(R.id.title_view);
        mContentView = (LinearLayout) findViewById(R.id.content_layout);
        mDateView = (TextView) findViewById(R.id.date_view);
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
        // as you specify A parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.load_news) {
            loadNews();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponseObtained(String title, String content, String date) {
        mTitleView.setText(title);
        placeContent(content, mContentView);
        mDateView.setText(date);
    }

    private void placeContent(String content, ViewGroup root) {
        new ContentMaker(root).place(content);
    }

    private void loadNews() {
        mNewsLoadingStarted = true;
        new NetworkDataLoader(this).load(DEFAULT_URL);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NEWS_ALREADY_SHOWN, mNewsLoadingStarted);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean loadingStarted = savedInstanceState.getBoolean(NEWS_ALREADY_SHOWN);
        if (loadingStarted) {
            loadNews();
        }
    }
}
