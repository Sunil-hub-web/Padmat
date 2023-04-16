package com.example.foodhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class TeramAndCondition extends AppCompatActivity {

    WebView webView;
    ImageView appclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teram_and_condition);


        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.webView);
        appclose = findViewById(R.id.appclose);

        appclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TeramAndCondition.this,MainActivity.class));
            }
        });

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://fudhub.co.in/terms-condition.html");

        WebSettings webSettings = webView.getSettings();
        webView.setVerticalScrollBarEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        //webView.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        webView.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
    }
}