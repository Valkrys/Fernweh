package com.example.fernweh;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomsActivity extends AppCompatActivity {
    WebView webView;
    String whereman;
    String countray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customs);

        whereman = getIntent().getStringExtra("label");

        webView = (WebView) findViewById(R.id.customs_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new MyWebViewClient());
        getValue();
        webView.loadUrl("https://www.gov.uk/foreign-travel-advice/"+countray+"/local-laws-and-customs");

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }
        });
    }

    public void visible(){
        WebView webview = (WebView) findViewById(R.id.customs_webview);
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
        TextView version = (TextView) findViewById(R.id.loading);
        webview.setVisibility(View.VISIBLE);
        bar.setVisibility(View.INVISIBLE);
        version.setVisibility(View.INVISIBLE);

    }

    public void invisible() {
        WebView webview = (WebView) findViewById(R.id.customs_webview);
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
        TextView version = (TextView) findViewById(R.id.loading);
        webview.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.VISIBLE);
        version.setVisibility(View.VISIBLE);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            webview.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            invisible();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            visible();
        }


    }
        public void getValue(){
        String selectQuery = "SELECT * FROM tripsTable WHERE tripName = '"+whereman+"'";
        DBHelper db = new DBHelper(this);
        SQLiteDatabase mydb = db.getWritableDatabase();

        Cursor cursor = mydb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                countray = cursor.getString(2).toLowerCase();
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

}
