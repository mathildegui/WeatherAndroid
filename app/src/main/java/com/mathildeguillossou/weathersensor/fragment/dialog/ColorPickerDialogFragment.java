package com.mathildeguillossou.weathersensor.fragment.dialog;

import com.github.danielnilsson9.colorpickerview.R;
import com.github.danielnilsson9.colorpickerview.view.ColorPanelView;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView.OnColorChangedListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ColorPickerDialogFragment extends DialogFragment {

    public interface ColorPickerDialogListener {
        void onColorSelected(int dialogId, int color);
        void onDialogDismissed(int dialogId);
    }


    private int mDialogId = -1;

    private ColorPickerView mColorPicker;
    private ColorPanelView mNewColorPanel;

    private ColorPickerDialogListener mListener;


    public static ColorPickerDialogFragment newInstance(int dialogId, int initialColor) {
        return newInstance(dialogId, null, null, initialColor, false);
    }

    public static ColorPickerDialogFragment newInstance(
            int dialogId, String title, String okButtonText, int initialColor, boolean showAlphaSlider) {

        ColorPickerDialogFragment frag = new ColorPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt("id", dialogId);
        args.putString("title", title);
        args.putString("ok_button", okButtonText);
        args.putBoolean("alpha", showAlphaSlider);
        args.putInt("init_color", initialColor);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogId = getArguments().getInt("id");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (ColorPickerDialogListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException("Parent activity must implement "
                    + "ColorPickerDialogListener to receive result.");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);

        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.colorpickerview__dialog_color_picker, container);


        TextView titleView = (TextView) v.findViewById(android.R.id.title);

        mColorPicker = (ColorPickerView)
                v.findViewById(R.id.colorpickerview__color_picker_view);
        ColorPanelView oldColorPanel = (ColorPanelView)
                v.findViewById(R.id.colorpickerview__color_panel_old);
        mNewColorPanel = (ColorPanelView)
                v.findViewById(R.id.colorpickerview__color_panel_new);
        Button okButton = (Button) v.findViewById(android.R.id.button1);


        mColorPicker.setOnColorChangedListener(new OnColorChangedListener() {

            @Override
            public void onColorChanged(int newColor) {
                mNewColorPanel.setColor(newColor);
            }
        });

        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onColorSelected(mDialogId, mColorPicker.getColor());
                getDialog().dismiss();
            }

        });

        String title = getArguments().getString("title");

        if(title != null) {
            titleView.setText(title);
        }
        else {
            titleView.setVisibility(View.GONE);
        }

        if(savedInstanceState == null) {
            mColorPicker.setAlphaSliderVisible(
                    getArguments().getBoolean("alpha"));


            String ok = getArguments().getString("ok_button");
            if(ok != null) {
                okButton.setText(ok);
            }

            int initColor = getArguments().getInt("init_color");

            oldColorPanel.setColor(initColor);
            mColorPicker.setColor(initColor, true);
        }

        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onDialogDismissed(mDialogId);
    }

}
