package com.tnt9.kiermasz.authorization;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import butterknife.OnTextChanged;

public class SignUpFragment extends Fragment {

    @BindView(R.id.edit_signup_email) EditText emailEditText;
    @BindView(R.id.edit_signup_password) EditText passwordEditText;
    @BindView(R.id.edit_signup_repeat_password) EditText repeatPasswordEditText;

    private FirebaseAuth mFirebaseAuth;
    private AuthorizationInterface mCallback;
    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (AuthorizationInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        mFirebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    @OnClick(R.id.card_signup_signup)
    public void cos(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();

        if (!password.equals(repeatPassword)){
            Toast.makeText(mActivity, "Passwords do not match", Toast.LENGTH_SHORT).show();

        }else if (password.length() < getResources().getInteger(R.integer.min_password_length)){
            Toast.makeText(mActivity, "Too short password", Toast.LENGTH_SHORT).show();

        }else if (!email.contains("@") || !email.contains(".") ||
                email.length() < getResources().getInteger(R.integer.min_email_length)){
            Toast.makeText(mActivity, "Wrong email", Toast.LENGTH_SHORT).show();

        }
        else {
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(mActivity, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                mCallback.signUp();
                            }
                        }
                    });
        }
    }

    @OnTextChanged(value = {R.id.edit_signup_password, R.id.edit_signup_repeat_password},
            callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void passwordChanged(){
        if (passwordEditText.getText().toString().equals(repeatPasswordEditText.getText().toString())){
            Toast.makeText(mActivity, "OK", Toast.LENGTH_SHORT).show();
        }
    }
}
