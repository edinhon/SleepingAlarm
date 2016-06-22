package com.example.nthucs.sleepingalarm;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;


public class ShopActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<View> viewList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop);

        mViewPager = (ViewPager) findViewById(R.id.ItemView);
        final LayoutInflater mInflater = getLayoutInflater().from(this);

        View v1 = mInflater.inflate(R.layout.item1_fragment, null);
        View v2 = mInflater.inflate(R.layout.item2_fragment, null);

        viewList = new ArrayList<View>();

        viewList.add(v1);
        viewList.add(v2);

        mViewPager.setAdapter(new PagerAdapterItem(viewList));
        mViewPager.setCurrentItem(0);

        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        radioGroup.check(R.id.radioButton);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
