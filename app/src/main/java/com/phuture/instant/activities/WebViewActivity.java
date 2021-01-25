package com.phuture.instant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phuture.instant.R;

public class WebViewActivity extends AppCompatActivity {

    public static final String PARAM_URL = "url";

    protected String url;
    protected WebView webView;
    protected ProgressBar progressBar;
    protected TextView title;

    void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        progressBar = findViewById(R.id.progress_bar);
        title = findViewById(R.id.title);

        showProgressBar(true);

        url = getIntent().getStringExtra(PARAM_URL);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!TextUtils.isEmpty(view.getTitle())) {
                    WebViewActivity.this.setTitle(view.getTitle());
                    title.setText(view.getTitle());
                }
                showProgressBar(false);
            }
        });

        webView.loadUrl(url);
    }
}