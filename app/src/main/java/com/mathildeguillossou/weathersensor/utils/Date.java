package com.mathildeguillossou.weathersensor.utils;

import java.util.Calendar;

/**
 * @author Mathilde Guillossou
 */
public class Date {
    public final static String DAY, DAWN, NIGHT, EVENING;

    static {
        DAY     = "day";
        DAWN    = "dawn";
        NIGHT   = "night";
        EVENING = "evening";
    }

    /**
     * Return the moment of the day
     * Possible values: NIGHT - DAWN - DAY - EVENING
     * @return Moment of the day
     */
    public static String getTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        if(hour >= 22 || hour < 4) {
            return NIGHT;
        } else if (hour >= 4 && hour < 10) {
            return DAWN;
        } else if (hour >= 10 && hour < 16) {
            return DAY;
        } else if (hour >= 16 && hour < 22) {
            return EVENING;
        }
        return DAY;
    }
}
