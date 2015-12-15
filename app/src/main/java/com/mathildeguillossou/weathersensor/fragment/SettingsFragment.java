package com.mathildeguillossou.weathersensor.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.danielnilsson9.colorpickerview.preference.ColorPreference;
import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.fragment.dialog.ColorPickerDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements
        ColorPickerDialogFragment.ColorPickerDialogListener {

    public static final int PREFERENCE_DIALOG_ID = 1250;

    public SettingsFragment() {

    }
    private ColorPreference mPref;
    private ColorNotEnableListener mListener;
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

        mPref = (ColorPreference) findPreference("pref_sync_color");
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //mPref.setSelectable(false);
            mPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mListener.onColorSelected(Build.VERSION.SDK_INT);
                    return false;
                }
            });
        } else {
            mPref.setOnShowDialogListener(new ColorPreference.OnShowDialogListener() {
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
                mPref.saveValue(color);
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
