package com.example.instagram.main.mainfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.instagram.R;
import com.example.instagram.adapter.adapterfirebase.ViewPageAdapter;

import java.util.ArrayList;

public class ViewPictureFireActivity extends AppCompatActivity {
    private ViewPager viewPagerPictureFire;
    private ViewPageAdapter pageAdapter;
    private ArrayList<String> listUrlPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view_picture_fire);

        setTitle("View Picture");

        viewPagerPictureFire = findViewById(R.id.viewPagerPictureFire);
        listUrlPicture = new ArrayList<>();

        Intent intent = getIntent();
        listUrlPicture = intent.getStringArrayListExtra("listurlstring");
        int position = intent.getIntExtra("position", -1);
        viewPagerPictureFire.setCurrentItem(position);

        pageAdapter = new ViewPageAdapter(listUrlPicture, this);

        viewPagerPictureFire.setAdapter(pageAdapter);
    }
}
