package com.mathildeguillossou.weathersensor.bean;

import java.util.Locale;

/**
 * @author Mathilde Guillossou
 */
public class Weather {
    //public String id;
    public float humidity;
    public float temperature;

    public Weather(float humidity, float temperature) {
        this.humidity    = humidity;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%f: temperature, %f: humidity", temperature, humidity);
    }
}
