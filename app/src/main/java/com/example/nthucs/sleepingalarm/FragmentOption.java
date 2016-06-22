package com.example.nthucs.sleepingalarm;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;


public class FragmentOption extends Fragment {


    private OnFragmentInteractionListener mListener;
    AudioManager am;
    Parameter_DBSet p_dbSet;
    ArrayList<Parameter> parameterList = new ArrayList<>();
    Parameter parameter;

    public FragmentOption(){}

    public static FragmentOption newInstance() {
        FragmentOption fragment = new FragmentOption();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /*switchStatus = (TextView) findViewById(R.id.switchStatus);
        mySwitch = (Switch) findViewById(R.id.vibration_switch);

        mySwitch.setChecked(true);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    switchStatus.setText("Switch is currently ON");
                }else{
                    switchStatus.setText("Switch is currently OFF");
                }

            }
        });

        //check the current state before we display the screen
        if(mySwitch.isChecked()){
            switchStatus.setText("Switch is currently ON");
        }
        else {
            switchStatus.setText("Switch is currently OFF");
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        p_dbSet = new Parameter_DBSet(getContext());
        if(p_dbSet.getCount() == 0){
            parameter = new Parameter(100, 0, 0);
            parameter = p_dbSet.insert(parameter);
            parameterList = p_dbSet.getAll();
        }
        //If not a new app, get Parameter.
        else {
            parameterList = p_dbSet.getAll();
            for(Parameter p : parameterList){
                parameter = p;
            }
        }

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_option, container, false);

        //Set volume.
        getActivity().setVolumeControlStream(AudioManager.STREAM_ALARM);

        am = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        SeekBar sb = (SeekBar)view.findViewById(R.id.loud_value);
        sb.setMax(am.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        sb.setProgress(am.getStreamVolume(AudioManager.STREAM_ALARM));

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                am.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Set vibrate.
        Switch s = (Switch)view.findViewById(R.id.vibration_switch);
        if (parameter.isVibratable()){
            s.setChecked(true);
        }else {
            s.setChecked(false);
        }

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    parameter.setVibratable(true);
                    p_dbSet.update(parameter);
                }else {
                    parameter.setVibratable(false);
                    p_dbSet.update(parameter);
                }
            }
        });




        return view;
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
