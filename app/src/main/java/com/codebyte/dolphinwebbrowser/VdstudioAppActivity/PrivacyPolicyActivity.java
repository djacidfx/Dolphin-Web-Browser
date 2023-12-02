package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import dolphinwebbrowser.R;


public class PrivacyPolicyActivity extends BaseActivity {
    ImageView imgbackWeb;
    private WebView webView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.privacy_policy);
        this.webView = (WebView) findViewById(R.id.webView);
        ImageView imageView = (ImageView) findViewById(R.id.imgback);
        this.imgbackWeb = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                PrivacyPolicyActivity.this.onBackPressed();
            }
        });
        loadData();
    }


    @Override
    public void onResume() {
        super.onResume();
        getWindow().clearFlags(16);
    }

    private void loadData() {
        this.webView.setBackgroundColor(Color.parseColor("#ffffff"));
        this.webView.setFocusableInTouchMode(false);
        this.webView.setFocusable(false);
        this.webView.getSettings().setDefaultTextEncodingName("UTF-8");
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient());
        this.webView.getSettings().setBuiltInZoomControls(true);
        WebSettings settings = this.webView.getSettings();
        settings.setDefaultFontSize(getResources().getInteger(R.integer.font_size));
        settings.setJavaScriptEnabled(true);
        this.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.webView.loadUrl("https://www.google.com");
    }
}
