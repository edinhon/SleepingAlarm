package com.example.nthucs.sleepingalarm;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
        initialisepage();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initialisepage(){

        List<Fragment> fragments = new Vector<Fragment>();

        fragments.add(Fragment.instantiate(this.getContext(),Item1.class.getName()));
        fragments.add(Fragment.instantiate(this.getContext(),Item2.class.getName()));

        /*mpagerAdapterItem = new PagerAdapterItem(this.getFragmentManager(),fragments);
        ViewPager pager = (ViewPager) this.getActivity().findViewById(R.id.ItemView);
        pager.setAdapter(mpagerAdapterItem);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        initialisepage();


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
