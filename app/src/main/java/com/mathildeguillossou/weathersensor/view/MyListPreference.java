package com.mathildeguillossou.weathersensor.view;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * @author mathildeguillossou
 * @since 15/12/15
 */
public class MyListPreference extends ListPreference {

    public MyListPreference(final Context context) {
        super(context, null);
    }

    public MyListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public CharSequence getSummary() {
        final CharSequence entry   = getEntry();
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
    }
}
