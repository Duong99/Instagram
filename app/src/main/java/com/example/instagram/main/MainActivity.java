package com.example.instagram.main;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.view.Window;

import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.instagram.LinkUrlApi;
import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.database.MyDatabase;
import com.example.instagram.model.Person;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private AsyncHttpClient client = new AsyncHttpClient();
    private ImageButton imgBtnLogin;
    private ProgressBar progress_circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


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

        checkCookie();
    }

    private void checkCookie() {
        String cookie = Utils.getCookieInstagram();
        if (cookie == null){
            imgBtnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.webview_login);

                    WebView webview = dialog.findViewById(R.id.webLogin);
                    webview.getSettings().setLoadsImagesAutomatically(true);
                    webview.setWebViewClient(new WebViewClient());
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                    webview.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            String cookie = Utils.getCookieInstagram();
                            Toast.makeText(MainActivity.this, "Cookie: " + cookie, Toast.LENGTH_SHORT).show();

                            if (cookie != null){
                                loginFinish(cookie);
                            }
                        }
                    });

                    webview.loadUrl(LinkUrlApi.URL_INSTAGRAM);
                    dialog.show();
                }
            });

        }else {
            loginFinish(cookie);
        }
    }

    private void loginFinish(String cookies){
        progress_circular.setVisibility(View.VISIBLE);
        client.addHeader("cookie", cookies);
        client.get(LinkUrlApi.URL_INSTAGRAM, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Document doc = Jsoup.parse(new String(responseBody));
                if (doc != null){

                    String oProfile = doc.html().split("_sharedData = ", 2)[1];
                    oProfile = oProfile.substring(0, oProfile.indexOf(";</script>"));

                    if (oProfile != null){
                        try {
                            JSONObject viewer = new JSONObject(oProfile).getJSONObject("config")
                                    .getJSONObject("viewer");

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
                Toast.makeText(MainActivity.this, "Check your network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
