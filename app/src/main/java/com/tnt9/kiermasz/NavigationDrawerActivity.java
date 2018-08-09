package com.tnt9.kiermasz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NavigationDrawerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
