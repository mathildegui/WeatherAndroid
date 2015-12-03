package com.mathildeguillossou.weathersensor.bean;

import java.util.Locale;

/**
 * Created by mathildeguillossou on 03/12/15.
 */
public class Weather {
    public final static String TAG = "Weather";

    //public String id;
    public float humidity;
    public float temperature;

    @Override
    public String toString() {
        return String.format(Locale.US, "%f: temperature, %f: humidity", temperature, humidity);
        //return String.format(Locale.US, "%s: id, %d: temperature, %d: humidity", id, temperature, humidity);
        /*return "Weather{" +
                "humidity=" + humidity +
                ", temperature=" + temperature +
                '}';*/
    }
}
