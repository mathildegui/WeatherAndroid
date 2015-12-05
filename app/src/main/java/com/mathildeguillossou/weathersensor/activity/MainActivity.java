package com.mathildeguillossou.weathersensor.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.api.ApiManager;
import com.mathildeguillossou.weathersensor.bean.Weather;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.subscriptions.Subscriptions;


public class MainActivity extends AppCompatActivity implements Observer<List<Weather>> {

    private List<Weather> mWeatherList;
    private Subscription mSubscription= Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWeatherList = new ArrayList<>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSubscription =
                AndroidObservable
                        .fromActivity(this, ApiManager.loadW())
                        .subscribe(this);
    }

    @Override
    protected void onDestroy() {
        mSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(List<Weather> args) {
        Log.d("Observer", "onNext " + args.toString());
        mWeatherList = args;
    }
}
