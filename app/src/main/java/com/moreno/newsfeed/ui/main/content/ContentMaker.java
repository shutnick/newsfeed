package com.moreno.newsfeed.ui.main.content;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moreno.newsfeed.network.imagedata.DownloadImageTask;
import com.moreno.newsfeed.util.ScaleUtil;
import com.moreno.newsfeed.util.html.HtmlAttr;
import com.moreno.newsfeed.util.html.HtmlTag;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class parses HTML fragment, define its components and places them into root view.
 *
 * Important!
 *
 * HTML fragment should only contain <p> tag with neither text content (formatted or not)
 * or link with clickable image; or <iframe> tag with video.
 */
public class ContentMaker {
    public final String TAG = getClass().getSimpleName();
    public static final int DEFAULT_FONT_SIZE = 16;
    public static final String HTML_MIME_TYPE = "text/html";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String COMMA = ",";
    private final ViewGroup mRoot;
    private final Context mContext;

    /**
     *
     * @param place Root view, where content will be placed
     */
    public ContentMaker(ViewGroup place) {
        mRoot = place;
        mContext = mRoot.getContext();
    }

    /**
     * Parse HTML fragment and place it to root view
     * @param content   HTML fragment for parsing
     */
    public void place(String content) {
        mRoot.removeAllViews();
        Document html = Jsoup.parseBodyFragment(content);
        Elements elements = html.select(HtmlTag.P + COMMA + HtmlTag.IFRAME);
        for (Element element : elements) {
            defineElement(element);
        }
    }

    /**
     * Place UI component according to data: TextView, ImageView or WebView
     * @param element
     */
    private void defineElement(Element element) {
//        Log.i(TAG, "tag name: " + element.tagName());
        String tag = element.tagName();
        if (HtmlTag.IFRAME.equals(tag)) {
            addVideo(element);
        } else if (HtmlTag.P.equals(tag)) {
//            Log.i(TAG, "Paragraph detected");
            if (element.hasText()) {
//                Log.i(TAG, "Paragraph has text");
                addText(element);
            } else {
//                Log.i(TAG, "Paragraph has image");
                addClickableImage(element);
            }
        }
    }

    private void addClickableImage(Element element) {
        final String link = element.select(HtmlTag.A).first().attr(HtmlAttr.HREF);
        Element imageElement = element.select(HtmlTag.IMG).first();
        String srcPath = imageElement.attr(HtmlAttr.SRC);

        TextView textView = new TextView(mContext);
        prepareAltText(imageElement, textView);

        ImageView imageView = new ImageView(mContext);
        prepareImageView(imageView, imageElement);

        new DownloadImageTask(mRoot, imageView, textView).execute(srcPath);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        };

        textView.setOnClickListener(clickListener);
        imageView.setOnClickListener(clickListener);

        mRoot.addView(textView);
        mRoot.addView(imageView);
    }

    private void prepareAltText(Element imageElement, TextView textView) {
        String altText = imageElement.attr(HtmlAttr.ALT);
        textView.setText(altText);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_FONT_SIZE);
    }

    private void prepareImageView(ImageView imageView, Element element) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);
    }

    private void addText(Element element) {
        TextView textView = new TextView(mContext);
        prepareTextView(textView, element);
        mRoot.addView(textView);
    }

    private void prepareTextView(TextView textView, Element data) {
        Spanned text = Html.fromHtml(data.toString());
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_FONT_SIZE);
    }

    private void addVideo(Element videoElement) {
        WebView webView = new WebView(mContext);
        prepareWebView(webView, videoElement);
        mRoot.addView(webView);
    }

    private void prepareWebView(WebView webView, Element videoElement) {
        int videoWidth = Integer.parseInt(videoElement.attr(HtmlAttr.WIDTH));
        int scale = ScaleUtil.getScale(mRoot.getContext(), videoWidth);
        webView.setInitialScale(scale);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        webView.setLayoutParams(params);

        WebSettings contentWebSettings = webView.getSettings();
        contentWebSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadData(videoElement.toString(), HTML_MIME_TYPE, DEFAULT_ENCODING);
    }
}
