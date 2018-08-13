package com.tnt9.kiermasz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tnt9.kiermasz.authorization.AuthorizationFragment;
import com.tnt9.kiermasz.authorization.AuthorizationInterface;
import com.tnt9.kiermasz.authorization.LoginFragment;
import com.tnt9.kiermasz.authorization.PasswordRecoveryFragment;
import com.tnt9.kiermasz.authorization.SignUpFragment;
import com.tnt9.kiermasz.drawer.NavigationDrawerActivity;

public class MainActivity extends AppCompatActivity implements AuthorizationInterface{

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String TAG_AUTHORIZATION = "authorization";
    private static final String TAG_LOGIN = "login";
    private static final String TAG_SIGNUP = "signup";
    private static final String TAG_PASSWORD_RECOVERY = "password_recovery";
    public static String CURRENT_TAG = TAG_AUTHORIZATION;
    private FirebaseAuth mAuth;

    // 1) Layout is moved when keyboard is closing.
    // 2) Add progress bar to authorization fragments.
    // 3) Reset password instructions are in bad contrast with background.

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

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
        updateUI(null);
    }

    @Override
    public void logIn() {
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void logInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 2);
    }

    @Override
    public void logInFacebook() {
    }

    @Override
    public void logInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        AuthorizationFragment myFragment = (AuthorizationFragment)getSupportFragmentManager().findFragmentByTag(TAG_AUTHORIZATION);
        if (myFragment != null && myFragment.isVisible()) finish();
        else super.onBackPressed();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 2) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(getApplicationContext(), "Log in fail", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "FAIL", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser){
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
