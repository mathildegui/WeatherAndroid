package com.mathildeguillossou.weathersensor.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * @author Mathilde Guillossou
 */
public class Android {
    public static void loadFragment(FragmentManager fm, Class c, int id, boolean replace, boolean addToStack) {
        Fragment f = null;
        try {
            f = (Fragment) c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(replace) {
            fm.beginTransaction().replace(id, f).commit();
        } else {
            if(addToStack) fm.beginTransaction().addToBackStack(c.getSimpleName()).add(id, f).commit();
            else fm.beginTransaction().add(id, f).commit();
        }
    }
}
