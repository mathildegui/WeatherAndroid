package com.mathildeguillossou.weathersensor.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mathildeguillossou.weathersensor.api.ApiClassInterface;
import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.api.ApiManager;
import com.mathildeguillossou.weathersensor.bean.Weather;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ApiClassInterface.ApiListInterface {

    private List<Weather> mWeatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getWeathers();
    }

    private void getWeathers() {
        ApiManager apiManager = new ApiManager(this);
        apiManager.loadW();
    }

    @Override
    public void success(List<?> list) {
        mWeatherList = new ArrayList<>();
        mWeatherList = (List<Weather>) list;
        Log.d("success", mWeatherList.toString());
    }

    @Override
    public void fail() {

    }
}
