package com.example.nthucs.sleepingalarm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;

/**
 * Created by NTHUCS on 2016/6/20.
 */
public class FragmentLogin extends Fragment {


    private Button batchRequestButton;
    private TextView textViewResults;
    private OnFragmentInteractionListener mListener;
    private LinearLayout temp = null;

    public FragmentLogin(){}
    public static FragmentLogin newInstance() {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        temp = (LinearLayout) view;
        view.setVisibility(view.VISIBLE);

        //LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        //authButton.setFragment(this);

        /*batchRequestButton = (Button) view.findViewById(R.id.batchRequestButton);
        batchRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBatchRequest();
            }
        });*/

        //textViewResults = (TextView) view.findViewById(R.id.textViewResults);
        //textViewResults.setText("");

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onBack(){
        if(temp.getVisibility() != View.VISIBLE) getActivity().finish();
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
