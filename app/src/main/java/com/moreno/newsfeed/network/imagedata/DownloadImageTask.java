package com.moreno.newsfeed.network.imagedata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.moreno.newsfeed.util.ScaleUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Download image, scale it to screen width, and place it to appropriate ImageView
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    public static final String GET_REQUEST = "GET";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READING_TIMEOUT = 20000;
    private WeakReference<ViewGroup> mRoot;
    private WeakReference<ImageView> mChild;
    private WeakReference<TextView> mOldChild;

    public DownloadImageTask(ViewGroup root, ImageView child, TextView oldChild) {
        mRoot = new WeakReference<>(root);
        mChild = new WeakReference<>(child);
        mOldChild = new WeakReference<>(oldChild);
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ViewGroup rootView = mRoot.get();
        ImageView childView = mChild.get();
        TextView oldView = mOldChild.get();
        if (rootView != null) {
            rootView.removeView(oldView);
        }
        if (childView != null) {
            childView.setImageBitmap(bitmap);
        }
    }

    private Bitmap downloadBitmap(String path) {
        InputStream responseContent = getStreamData(path);
        return decodeBitmap(responseContent);
    }

    private Bitmap decodeBitmap(InputStream responseContent) {
        Display display = ((WindowManager) mRoot.get().getContext()
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenWidth = display.getWidth();
        Bitmap result = BitmapFactory.decodeStream(responseContent);
        int scale = ScaleUtil.getScale(mRoot.get().getContext(), result.getWidth());
        result = Bitmap.createScaledBitmap(result, screenWidth, scale * result.getHeight() / 100,
                false);
        Log.i("tag", "Bitmap size: " + (result.getRowBytes() * result.getHeight() / 1024) + " KB");
        return result;
    }

    private InputStream getStreamData(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET_REQUEST);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READING_TIMEOUT);
            connection.setDoInput(true);
            connection.connect();
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
