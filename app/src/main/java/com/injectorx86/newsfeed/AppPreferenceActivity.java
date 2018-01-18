package com.injectorx86.newsfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.injectorx86.newsfeed.fragments.SettingsFragment;

public class AppPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.changeFragment(getSupportFragmentManager(), new SettingsFragment());
    }
}

