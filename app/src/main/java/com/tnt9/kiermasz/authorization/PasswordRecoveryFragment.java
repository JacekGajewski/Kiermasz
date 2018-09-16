package com.tnt9.kiermasz.authorization;

import android.app.Activity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.tnt9.kiermasz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordRecoveryFragment extends Fragment {

    @BindView(R.id.edit_password_recovery_email) EditText emailEditText;
    private Activity mActivity;
    private FirebaseAuth mFirebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_password_recovery, container, false);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        mFirebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    @OnClick(R.id.card_password_recovery_recover)
    public void recoverPasswordClicked(){
        String email = emailEditText.getText().toString();
        if (!email.contains("@") || !email.contains(".") ||
                email.length() < getResources().getInteger(R.integer.min_email_length)){
            Toast.makeText(mActivity, "Wrong email", Toast.LENGTH_SHORT).show();
        }
        else {
            mFirebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(mActivity, "Instructions to reset your password have been sent1", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mActivity, "Failed to send reset password email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
