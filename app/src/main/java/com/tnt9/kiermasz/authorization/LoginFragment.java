package com.tnt9.kiermasz.authorization;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tnt9.kiermasz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    @BindView(R.id.edit_login_email) EditText emailEditText;
    @BindView(R.id.edit_login_password) EditText passwordEditText;

    private AuthorizationInterface mCallback;
    private FirebaseAuth auth;
    Activity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (AuthorizationInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();

        auth = FirebaseAuth.getInstance();
        return view;
    }


    @OnClick(R.id.card_login_login)
    public void loginClicked(){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (password.length() < getResources().getInteger(R.integer.min_password_length)){
            Toast.makeText(activity, "Too short password", Toast.LENGTH_SHORT).show();

        }else if (!email.contains("@") || !email.contains(".") ||
                email.length() < getResources().getInteger(R.integer.min_email_length)){
            Toast.makeText(activity, "Wrong email", Toast.LENGTH_SHORT).show();

        }
        else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mCallback.logIn();
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.text_login_anonymously)
    public void loginAnonymouslyClicked(){
        mCallback.logInAnonymously();
    }

    @OnClick(R.id.text_login_forgot_password)
    public void forgotPasswordClicked(){
        mCallback.passwordForgottenClicked();
    }

}
