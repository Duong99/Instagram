package com.example.instagram.main;

import com.example.instagram.dialog.EnterPassword;
import com.example.instagram.database.FireDatabase;
import com.example.instagram.database.MyDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.instagram.LoadingSearchFriend;
import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.adapter.FragmentsAdapter;

import com.example.instagram.main.mainmusic.MainMusicActivity;
import com.example.instagram.model.Person;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoginSuccessActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentsAdapter adapter;
    private String id, userName;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        init();
        adapter = new FragmentsAdapter(getSupportFragmentManager(), getId());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init() {
        Intent intent = getIntent();
        setId(intent.getStringExtra("id"));
        setUserName(intent.getStringExtra("username"));
        setTitle(getUserName());
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public void onBackPressed() {
        if (getId().equals(Utils.getUserIdInstagram())) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_events, menu);

        String url_picture = new MyDatabase(LoginSuccessActivity.this).getPersonAnalyzed(Utils.getUserIdInstagram()).getImvPeron();
        new DownloadImgTask().execute(url_picture);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSearchFriend:
                final Dialog dialog = new Dialog(LoginSuccessActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_layout_search_people);

                ImageButton imvbtnCloseSearchFriend = dialog.findViewById(R.id.imvbtnCloseSearchFriend);
                EditText edtSearch = dialog.findViewById(R.id.edtSearchFriend);
                final RecyclerView recyclerFoundPeople = dialog.findViewById(R.id.recyclerFoundPeople);

                edtSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 0) {
                            new LoadingSearchFriend(LoginSuccessActivity.this, recyclerFoundPeople)
                                    .searchfriend(String.valueOf(s));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                imvbtnCloseSearchFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;

            case R.id.itemMeAccount:
                if (!getId().equals(Utils.getUserIdInstagram())) {
                    MyDatabase db = new MyDatabase(this);
                    Intent intent = new Intent(this, LoginSuccessActivity.class);
                    intent.putExtra("id", db.getPersonAnalyzed(Utils.getUserIdInstagram()).getIdPerson());
                    intent.putExtra("username", db.getPersonAnalyzed(Utils.getUserIdInstagram()).getUsername());
                    startActivity(intent);
                }
                break;

            case R.id.item_menu_music:
                Intent intent = new Intent(LoginSuccessActivity.this, MainMusicActivity.class);
                startActivity(intent);
                break;

            case R.id.itemPictureFire:
                new EnterPassword(this, new Person(id, userName, "", ""));

                break;

            case R.id.itemLogout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notification");
                builder.setMessage("You Want To Log Out App");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.webkit.CookieManager.getInstance().removeAllCookies(null);
                        CookieManager.getInstance().flush();
                        Intent intent = new Intent(LoginSuccessActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadImgTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            menu.findItem(R.id.itemProfile).setIcon(new BitmapDrawable(getResources(), bitmap));
        }
    }
}
