package com.tnt9.kiermasz.authorization;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt9.kiermasz.R;

import butterknife.BindView;
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
        View view =  inflater.inflate(R.layout.constr, container, false);
        ButterKnife.bind(this, view);
//        view.setPadding(0,0,0,500);
        return view;
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
    public void textPasswordForgottenClicked(){
        mCallback.passwordForgottenClicked();
    }
}
