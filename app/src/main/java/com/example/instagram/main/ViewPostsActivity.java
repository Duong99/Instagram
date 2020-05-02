package com.example.instagram.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.instagram.Common;
import com.example.instagram.R;

public class ViewPostsActivity extends AppCompatActivity {
    private WebView webviewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_posts_webview);

        webviewPost = findViewById(R.id.webviewPost);

        Intent intent = getIntent();
        String url = intent.getStringExtra("UserNameProfilePost");

        webviewPost.getSettings().setLoadsImagesAutomatically(true);
        webviewPost.setWebViewClient(new WebViewClient());
        webviewPost.getSettings().setJavaScriptEnabled(true);
        webviewPost.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webviewPost.loadUrl(url);
    }
}
