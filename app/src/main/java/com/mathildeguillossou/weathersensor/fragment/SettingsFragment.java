package com.mathildeguillossou.weathersensor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mathildeguillossou.weathersensor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
