package com.mathildeguillossou.weathersensor.view;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * @author mathildeguillossou
 * @since 15/12/15
 */
public class MyEditTextPreference extends DialogPreference {
    public final static String TAG = "MyEditTextPreference";

    public MyEditTextPreference(Context context) {
        super(context, null);
    }

    public MyEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
   /* @Override
    public CharSequence getSummary() {
        //final CharSequence entry   = get();
        get
        final CharSequence summary = super.getSummary();
        if(summary == null || entry == null) {
            return null;
        } else {
            return String.format(summary.toString(), entry);
        }
    }
    @Override
    public void setValue(final String value) {
        super.setValue(value);
        notifyChanged();
    }*/
}
