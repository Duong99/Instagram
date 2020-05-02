package com.example.instagram.main.mainmusic;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.instagram.R;
import com.example.instagram.adapter.adaptermusic.AdapterFragment;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainMusicActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterFragment adapterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);

        setTitle("Me Music");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        adapterFragment = new AdapterFragment(getSupportFragmentManager());
        viewPager.setAdapter(adapterFragment);
        tabLayout.setupWithViewPager(viewPager);

    }
}
