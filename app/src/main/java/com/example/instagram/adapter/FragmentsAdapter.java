package com.example.instagram.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.instagram.Utils;
import com.example.instagram.fragment.FilterFriendFragment;
import com.example.instagram.fragment.HomeFragment;
import com.example.instagram.fragment.PersonalFragment;
import com.example.instagram.fragment.SettinglFragment;


public class FragmentsAdapter extends FragmentStatePagerAdapter {
    private FilterFriendFragment filterFriendFragment;
    private PersonalFragment personalFragment;
    private SettinglFragment settinglFragment;
    private HomeFragment homeFragment;

    private String  lTitle1[] = {"Personal", "Home", "Filter", "Setting"};
    private String  lTitle2[] = {"Personal", "Filter", "Setting"};
    private String mId;

    public FragmentsAdapter(FragmentManager fm, String id) {
        super(fm);
        this.mId = id;
        if (Utils.getUserIdInstagram().equals(mId)){
            personalFragment = new PersonalFragment();
            homeFragment = new HomeFragment();
            filterFriendFragment = new FilterFriendFragment();
            settinglFragment = new SettinglFragment();
        }else {
            personalFragment = new PersonalFragment();
            filterFriendFragment = new FilterFriendFragment();
            settinglFragment = new SettinglFragment();
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (Utils.getUserIdInstagram().equals(mId)){
            switch (position){
                case 0:
                    return personalFragment;
                case 1:
                    return homeFragment;
                case 2:
                    return filterFriendFragment;
                case 3:
                    return settinglFragment;
            }
        }else {
            switch (position){
                case 0:
                    return personalFragment;
                case 1:
                    return filterFriendFragment;
                case 2:
                    return settinglFragment;
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        if (Utils.getUserIdInstagram().equals(mId)){
            return lTitle1.length;
        }else {
            return lTitle2.length;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (Utils.getUserIdInstagram().equals(mId)){
            return lTitle1[position];
        }else {
            return lTitle2[position];
        }


    }
}
