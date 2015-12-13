package com.mathildeguillossou.weathersensor.utils;

/**
 * Created by mathildeguillossou on 13/12/15.
 */
public class Number {
    public final static String TAG = "Number";

    public static boolean isNullDecimal(float f) {
        return (f - (int)f == 0);
    }
}
