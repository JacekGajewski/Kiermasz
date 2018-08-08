package com.tnt9.kiermasz;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tnt9.kiermasz.authorization.AuthorizationFragment;
import com.tnt9.kiermasz.authorization.AuthorizationInterface;
import com.tnt9.kiermasz.authorization.LoginFragment;
import com.tnt9.kiermasz.authorization.PasswordRecoveryFragment;
import com.tnt9.kiermasz.authorization.SignUpFragment;

public class MainActivity extends AppCompatActivity implements AuthorizationInterface{


    private static final String TAG_AUTHORIZATION = "authorization";
    private static final String TAG_LOGIN = "login";
    private static final String TAG_SIGNUP = "signup";
    private static final String TAG_PASSWORD_RECOVERY = "password_recovery";
    public static String CURRENT_TAG = TAG_AUTHORIZATION;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CURRENT_TAG = TAG_AUTHORIZATION;
        startFragment(new AuthorizationFragment());
    }

    @Override
    public void logInClicked() {
        CURRENT_TAG = TAG_LOGIN;
        startFragment(new LoginFragment());
    }

    @Override
    public void signUpClicked() {
        CURRENT_TAG = TAG_SIGNUP;
        startFragment(new SignUpFragment());
    }

    @Override
    public void passwordForgottenClicked() {
        CURRENT_TAG = TAG_PASSWORD_RECOVERY;
        startFragment(new PasswordRecoveryFragment());
    }

    @Override
    public void signUp() {
        startActivity(new Intent(this, Main2Activity.class));
    }

    @Override
    public void logIn() {

    }

    public void startFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                android.R.anim.fade_out);
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out );
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)
                            .addToBackStack(null)
                            .commit();
//        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        AuthorizationFragment myFragment = (AuthorizationFragment)getSupportFragmentManager().findFragmentByTag(TAG_AUTHORIZATION);
        if (myFragment != null && myFragment.isVisible()) finish();
        else super.onBackPressed();
    }
}
