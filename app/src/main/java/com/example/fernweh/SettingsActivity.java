package com.example.fernweh;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Turn off notifications setting

        WebView wv;
        wv = (WebView) findViewById(R.id.manual_webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.loadUrl("file:///android_asset/UserManual.html");
    }
}
