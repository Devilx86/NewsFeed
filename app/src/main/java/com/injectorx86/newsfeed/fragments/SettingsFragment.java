package com.injectorx86.newsfeed.fragments;

import android.os.Bundle;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import com.injectorx86.newsfeed.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferencesFix(Bundle bundle, String rootKey) {
        addPreferencesFromResource(R.xml.app_settings);
    }
}
