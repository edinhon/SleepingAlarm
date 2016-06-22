package com.example.nthucs.sleepingalarm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by NTHUCS on 2016/6/20.
 */
public class FragmentLogin extends Fragment {


    private TextView textViewResults;

    private OnFragmentInteractionListener mListener;
    private LinearLayout temp = null;
    private AccessToken accessToken;

    public FragmentLogin(){}
    public static FragmentLogin newInstance() {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        temp = (LinearLayout) view;
        view.setVisibility(view.VISIBLE);

        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("66666666666666666", "success");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                try {
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    ImageView userPhoto = (ImageView)getActivity().findViewById(R.id.userPhoto);
                                    if (object.has("picture")) {
                                        String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                        Picasso.with(getActivity()).load(profilePicUrl).into(userPhoto);
                                    }
                                    Log.d("email", email);
                                    Log.d("bir", birthday);
                                    TextView userName = (TextView)getActivity().findViewById(R.id.userName);
                                    userName.setText(name);
                                    TextView userEmail = (TextView)getActivity().findViewById(R.id.userEmail);
                                    userEmail.setText(email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.v("888888888888888888", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("8888888888888888888", exception.getCause().toString());
            }
        });

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
