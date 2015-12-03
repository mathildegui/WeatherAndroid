package com.mathildeguillossou.weathersensor.api;

import java.util.List;

/**
 * Created by mathildeguillossou on 04/12/15.
 */
public class ApiClassInterface {
    public final static String TAG = "ApiClassInterface";

    public interface ApiListInterface {
        void success(List<?> list);
        void fail();
    }
    public interface ApiInterface {
        void success(Object object);
        void fail();
    }
}
