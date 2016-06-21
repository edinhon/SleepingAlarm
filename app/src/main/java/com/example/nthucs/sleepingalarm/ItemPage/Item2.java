package com.example.nthucs.sleepingalarm.ItemPage;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nthucs.sleepingalarm.R;

public class Item2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(container == null){
            return null;
        }

        return inflater.inflate(R.layout.item2_fragment, container, false);
    }
}