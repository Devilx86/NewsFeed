package com.injectorx86.newsfeed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.injectorx86.newsfeed.fragments.NewsFeedFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(getSupportFragmentManager(), new NewsFeedFragment());
    }

    public static void changeFragment(FragmentManager fragmentManager, Fragment fragment) {

        Fragment fragmentContainer = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragmentContainer == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}