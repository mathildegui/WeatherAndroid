package com.mathildeguillossou.weathersensor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.api.ApiManager;
import com.mathildeguillossou.weathersensor.bean.Weather;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.android.observables.AndroidObservable;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements Observer<List<Weather>> {

    private Weather mWeather;
    private Subscription mSubscription = Subscriptions.empty();

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        /**
         * FIXME load only the last value and not the entire list of data
         */
        mSubscription =
                AndroidObservable
                        .fromFragment(this, ApiManager.loadW())
                        .observeOn(Schedulers.threadPoolForIO())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(this);

        return v;
    }

    @Override
    public void onDestroy() {
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
        mWeather = args.get(args.size() - 1);
    }

    private void updateData() {

    }
}