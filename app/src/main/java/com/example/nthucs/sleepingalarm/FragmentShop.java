package com.example.nthucs.sleepingalarm;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nthucs.sleepingalarm.ItemPage.Item1;
import com.example.nthucs.sleepingalarm.ItemPage.Item2;

import java.util.List;
import java.util.Vector;


public class FragmentShop extends Fragment {

    private OnFragmentInteractionListener mListener;
    private PagerAdapterItem mpagerAdapterItem;

    ViewPager pager;
    TabLayout tabLayout;
    public FragmentShop(){}

    public static FragmentShop newInstance() {
        FragmentShop fragment = new FragmentShop();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*pager= (ViewPager) this.getActivity().findViewById(R.id.ItemView);
        tabLayout= (TabLayout) this.getActivity().findViewById(R.id.tab_layout);

        // Fragment manager to add fragment in viewpager we will pass object of Fragment manager to adpater class.
        android.support.v4.app.FragmentManager manager = getChildFragmentManager();

        //object of PagerAdapter passing fragment manager object as a parameter of constructor of PagerAdapter class.
        PagerAdapterItem adapter=new PagerAdapterItem(manager);

        //set Adapter to view pager
        pager.setAdapter(adapter);

        //set tablayout with viewpager
        tabLayout.setupWithViewPager(pager);

        // adding functionality to tab and viewpager to manage each other when a page is changed or when a tab is selected
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Setting tabs from adpater
        tabLayout.setTabsFromPagerAdapter(adapter);
        */
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
