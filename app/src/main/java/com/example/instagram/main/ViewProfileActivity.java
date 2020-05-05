package com.example.instagram.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.instagram.LinkUrlApi;
import com.example.instagram.R;

public class ViewProfileActivity extends AppCompatActivity {

    private WebView webViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        webViewProfile = findViewById(R.id.webViewProfile);

        Intent intent = getIntent();
        String url = LinkUrlApi.URL_INSTAGRAM + intent.getStringExtra("UserNameProfilePost");


        webViewProfile.getSettings().setLoadsImagesAutomatically(true);
        webViewProfile.setWebViewClient(new WebViewClient());
        webViewProfile.getSettings().setJavaScriptEnabled(true);
        webViewProfile.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webViewProfile.loadUrl(url);

    }
}
