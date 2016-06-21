package com.example.nthucs.sleepingalarm;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

//http://www.androidwarriors.com/2015/10/tablayout-with-viewpager-android.html

public class PagerAdapterItem extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public PagerAdapterItem(FragmentManager fm, List<android.support.v4.app.Fragment> fragment){
        super(fm);
        this.fragments = fragment;
    }


    @Override
    public Fragment getItem(int argo0){
        return this.fragments.get(argo0);
    }

    @Override
    public int getCount(){
        return this.fragments.size();
    }

}
