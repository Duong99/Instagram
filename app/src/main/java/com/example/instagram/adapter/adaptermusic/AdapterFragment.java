package com.example.instagram.adapter.adaptermusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.instagram.fragment.fragmentmusic.FragmentLove;
import com.example.instagram.fragment.fragmentmusic.FragmentTheme;

public class AdapterFragment extends FragmentStatePagerAdapter {
    private FragmentLove fragmentLove;
    private FragmentTheme fragmentTheme;

    private String tList[] = {"Love", "Theme"};

    public AdapterFragment(@NonNull FragmentManager fm) {
        super(fm);
        fragmentLove = new FragmentLove();
        fragmentTheme = new FragmentTheme();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragmentLove;
            case 1:
                return fragmentTheme;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tList.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tList[position];
    }
}
