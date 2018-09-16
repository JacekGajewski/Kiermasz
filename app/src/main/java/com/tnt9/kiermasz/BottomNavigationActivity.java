package com.tnt9.kiermasz;

import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.tnt9.kiermasz.drawer.NavigationDrawerActivity;

public class BottomNavigationActivity extends AppCompatActivity {

    private static final String TAG = BottomNavigationActivity.class.getSimpleName();
    private static final String TAG_HOME = "home";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_FIRE = "fire";
    private static final String TAG_LIBRARY = "library";
    private static final String TAG_PROFILE = "profile";
    public static String CURRENT_TAG = "";
    private static final String FRAGMENT_KEY = "fragment_key";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar_bottom_navigation);
        setSupportActionBar(toolbar);


        if (savedInstanceState != null){
            CURRENT_TAG = savedInstanceState.getString(FRAGMENT_KEY);
        }
        startFragment();

        bottomNavigationView = findViewById(R.id.BottomNavigationView_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        CURRENT_TAG = TAG_HOME;
                        startFragment();
                        return true;
                    case R.id.action_fire:

                        return true;
                    case R.id.action_location:

                        return true;
                    case R.id.action_library:
                        CURRENT_TAG = TAG_LIBRARY;
                        startFragment();
                        return true;
                    case R.id.action_profile:
                        CURRENT_TAG = TAG_PROFILE;
                        startFragment();
                        return true;
                }
                return false;
            }
        });

//       bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_drawer:
                startActivity(new Intent(getApplicationContext(), NavigationDrawerActivity.class));
                return true;
            case R.id.action_add:
                startActivity(new Intent(getApplicationContext(), AddBookActivity.class));
                return true;
            case R.id.action_logout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                   auth.signOut();
                    startActivity(new Intent(BottomNavigationActivity.this,
                            MainActivity.class));
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FRAGMENT_KEY, CURRENT_TAG);
        super.onSaveInstanceState(outState);
    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    private void startFragment(){

        Fragment fragment;
        switch (CURRENT_TAG) {
            case TAG_HOME:
                fragment = new StaggeredFragment();
                break;
            case TAG_LOCATION:
//                startActivity(new Intent(getApplicationContext(), AddBookActivity.class));
            case TAG_FIRE:
//                startActivity(new Intent(getApplicationContext(), NavigationDrawerActivity.class));
            case TAG_LIBRARY:
                fragment = new LibraryFragment();
                break;
            case TAG_PROFILE:
                fragment = new ProfileFragment();
                break;
            default:
                fragment = new StaggeredFragment();
        }

        showAppLayout();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final int count = fragmentManager.getBackStackEntryCount();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                android.R.anim.fade_out);
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out );
        fragmentTransaction.replace(R.id.frame2, fragment, CURRENT_TAG)
                .addToBackStack(CURRENT_TAG)
                .commit();
//        fragmentTransaction.commitAllowingStateLoss();

//        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                if( fragmentManager.getBackStackEntryCount() <= count){
//                    // pop all the fragment and remove the listener
//                    fragmentManager.popBackStack(FRAGMENT_OTHER, POP_BACK_STACK_INCLUSIVE);
//                    fragmentManager.removeOnBackStackChangedListener(this);
//                    // set the home button selected
//                    navigation.getMenu().getItem(0).setChecked(true);
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (CURRENT_TAG.equals(TAG_HOME)) finish();
        CURRENT_TAG = TAG_HOME;
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        startFragment();
    }


    public void showAppLayout(){
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout_bottomNavigation);
        appBarLayout.setExpanded(true);
    }
}
