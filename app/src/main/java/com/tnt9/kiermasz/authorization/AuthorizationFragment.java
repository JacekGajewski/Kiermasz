package com.tnt9.kiermasz.authorization;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt9.kiermasz.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthorizationFragment extends Fragment {


    AuthorizationInterface mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (AuthorizationInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_authorization, container, false);
        ButterKnife.bind(this, view);
//        view.setPadding(0,0,0,500);
        return view;
    }

    @OnClick(R.id.card_authorization_facebook)
    public void cardFacebookClicked(){
        mCallback.logInFacebook();
    }

    @OnClick(R.id.card_authorization_google)
    public void cardGoogleClicked(){
        mCallback.logInGoogle();
    }

    @OnClick(R.id.card_authorization_login)
    public void cardLogInClicked(){
        mCallback.logInClicked();
    }

    @OnClick(R.id.card_authorization_signup)
    public void cardSignUpClicked(){
        mCallback.signUpClicked();
    }

    @OnClick(R.id.text_authorization_anonymously)
    public void textLogInAnonymouslyClicked(){
        mCallback.logInAnonymously();
    }

    @OnClick(R.id.text_main_forgotpassword)
    public void textPasswordForgottenClicked(){
        mCallback.passwordForgottenClicked();
    }
}
