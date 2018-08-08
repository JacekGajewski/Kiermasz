package com.tnt9.kiermasz.authorization;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tnt9.kiermasz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends Fragment {

    private FirebaseAuth auth;
    private AuthorizationInterface mCallback;
    Activity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (AuthorizationInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.constraint_signup, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        auth = FirebaseAuth.getInstance();

        return view;
    }

    @OnClick(R.id.card_authorization_login)
    public void cos(){
        String email = "";
        String password = "";

//        auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Toast.makeText(activity, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(activity, "Authentication failed." + task.getException(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
////                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
////                            finish();
//                        }
//                    }
//                });
        Toast.makeText(activity, "createUserWithEmail:onComplete:", Toast.LENGTH_SHORT).show();
        mCallback.signUp();
    }
}
