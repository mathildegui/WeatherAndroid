package com.mathildeguillossou.weathersensor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by mathildeguillossou on 12/12/15.
 */
public class Connection {

    /**
     * Determine if the application is connected to the internet or not
     * @param c The current context
     * @return False if the network is down, true if the network is okay
     */
    public static boolean checkActiveNetwork(Context c) {
        return getNetwork(c) != null &&
                getNetwork(c).isConnectedOrConnecting();
    }

    /**
     * Get the current active network
     * @param c The current context
     * @return active network
     */
    private static NetworkInfo getNetwork(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static String getErrorMessage() {
        return "The network is not avalaible right now. Please check your internet connection";
    }

    public static int getType(Context c) {
        return (getNetwork(c)!= null)? getNetwork(c).getType() : -1;
    }
}
