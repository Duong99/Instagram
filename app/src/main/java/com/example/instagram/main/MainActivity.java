package com.example.instagram.main;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;


import com.example.instagram.Common;
import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.database.MyDatabase;
import com.example.instagram.model.Person;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private String cookie = null;
    private WebView webview;
    private AsyncHttpClient client = new AsyncHttpClient();
    private ImageButton imgBtnLogin;
    private ProgressBar progress_circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

/*        try {
            PackageInfo info = null;
            try {
                info = getPackageManager().getPackageInfo(
                        "com.example.instagram",                  //Insert your own package name.
                        PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {

        }*/

        imgBtnLogin = findViewById(R.id.imgBtnLogin);
        progress_circular = findViewById(R.id.progress_circular);

        cookie = Utils.getCookieInstagram();

        checkCookie(cookie);

    }


    private void checkCookie(final String cookies) {
        if (cookie == null){
            imgBtnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.activity_view_profile_posts_webview);

                    webview = dialog.findViewById(R.id.webviewPost);

                    webview.getSettings().setLoadsImagesAutomatically(true);
                    webview.setWebViewClient(new WebViewClient());
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webview.loadUrl(Common.URL_INSTAGRAM);

                    ImageButton ibtnCloseWebviewLogin = dialog.findViewById(R.id.ibtnCloseWebviewLogin);
                    ibtnCloseWebviewLogin.setVisibility(View.VISIBLE);

                    ibtnCloseWebviewLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            });

        }else {
            progress_circular.setVisibility(View.VISIBLE);
            client.addHeader("cookie", cookies);
            client.get(Common.URL_INSTAGRAM, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Document doc = Jsoup.parse(new String(responseBody));
                    if (doc != null){

                        String oProfile = doc.html().split("_sharedData = ", 2)[1];
                        oProfile = oProfile.substring(0, oProfile.indexOf(";</script>"));

                        if (oProfile != null){
                            try {
                                JSONObject viewer = new JSONObject(oProfile).getJSONObject("config").getJSONObject("viewer");

                                MyDatabase db = new MyDatabase(MainActivity.this);
                                if (db.getPersonAnalyzed(viewer.getString("id")) == null){
                                    db.addPersonAnalyzed(new Person(viewer.getString("id"),
                                            viewer.getString("username"),
                                            viewer.getString("full_name"),
                                            viewer.getString("profile_pic_url_hd")));

                                }
                                Intent intent = new Intent(MainActivity.this, LoginSuccessActivity.class);
                                intent.putExtra("id", viewer.getString("id"));
                                intent.putExtra("username", viewer.getString("username"));
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("Duong", "onFailure: " + error.toString());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
