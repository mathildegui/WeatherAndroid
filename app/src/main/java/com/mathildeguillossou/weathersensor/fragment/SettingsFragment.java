package com.mathildeguillossou.weathersensor.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Switch;

import com.github.danielnilsson9.colorpickerview.preference.ColorPreference;
import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.fragment.dialog.ColorPickerDialogFragment;
import com.mathildeguillossou.weathersensor.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements
        ColorPickerDialogFragment.ColorPickerDialogListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int PREFERENCE_DIALOG_ID = 1250;

    public SettingsFragment() {

    }
    private ColorPreference mPrefColor;
    private SwitchPreference mPrefSwitchHum;
    private SwitchPreference mPrefSwitchTemp;
    private EditTextPreference mPrefEditTextUri;

    private ColorNotEnableListener mListener;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("onSharedPreferenceChanged", "onSharedPreferenceChanged");
    }

    public interface ColorNotEnableListener {
        void onColorSelected(int androidVersion);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ColorNotEnableListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException("Parent activity must implement "
                    + "ColorNotEnableListener to receive result.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        PreferenceManager.getDefaultSharedPreferences(
                this.getContext()).registerOnSharedPreferenceChangeListener(this);

        mPrefColor       = (ColorPreference) findPreference("pref_sync_color");
        mPrefSwitchHum   = (SwitchPreference) findPreference("pref_sync_hum");
        mPrefSwitchTemp  = (SwitchPreference) findPreference("pref_sync_temp");
        mPrefEditTextUri = (EditTextPreference) findPreference("pref_sync_uri");

        mPrefEditTextUri.setText(Constant.URI_MONTROUGE);
        mPrefEditTextUri.setSummary(Constant.URI_MONTROUGE);
        
        mPrefEditTextUri.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mPrefEditTextUri.setSummary(newValue.toString());
                return false;
            }
        });

        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mPrefColor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mListener.onColorSelected(Build.VERSION.SDK_INT);
                    return false;
                }
            });
        } else {
            mPrefColor.setOnShowDialogListener(new ColorPreference.OnShowDialogListener() {
                @Override
                public void onShowColorPickerDialog(String title, int currentColor) {
                    ColorPickerDialogFragment dialogFragment =
                            ColorPickerDialogFragment.newInstance(PREFERENCE_DIALOG_ID,
                                    "Color picker",
                                    null,
                                    currentColor, false);
                    dialogFragment.setStyle(DialogFragment.STYLE_NORMAL,
                            R.style.LightPickerDialogTheme);
                    dialogFragment.show(getFragmentManager(), "pre_dialog");
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case PREFERENCE_DIALOG_ID:
                getActivity().getWindow().setStatusBarColor(color);
                getActivity().getWindow().setNavigationBarColor(color);
                mPrefColor.saveValue(color);
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
