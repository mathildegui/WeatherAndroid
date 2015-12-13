package com.mathildeguillossou.weathersensor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.api.ApiManager;
import com.mathildeguillossou.weathersensor.bean.Weather;
import com.mathildeguillossou.weathersensor.utils.*;
import com.mathildeguillossou.weathersensor.utils.Number;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.android.observables.AndroidObservable;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements Observer<Weather> {

    @Bind(R.id.currentTemp) TextView mCurrentTemp;
    @Bind(R.id.currentHumidity) TextView mCurrentHumidity;

    private Weather mWeather;
    private Subscription mSubsGetLast = Subscriptions.empty();

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, v);

        /**
         * FIXME load only the last value and not the entire list of data
         */
        mSubsGetLast =
                AndroidObservable
                        .fromFragment(this, ApiManager.getLast())
                        .observeOn(Schedulers.threadPoolForIO())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(this);
        return v;
    }

    @Override
    public void onDestroy() {
        mSubsGetLast.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Weather args) {
        mWeather = args;
        new ReceiverThread().run();
    }

    /**
     * Because only the original thread that created a view hierarchy can touch its views
     */
    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            MainFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String t = Number.isNullDecimal(mWeather.temperature)?
                            String.valueOf(mWeather.temperature):
                            String.format("%.2f", mWeather.temperature);
                            mCurrentTemp.setText(t);
                    String h = Number.isNullDecimal(mWeather.humidity)?
                            String.valueOf(mWeather.humidity):
                            String.format("%.2f", mWeather.humidity);
                    mCurrentHumidity.setText(h);
                }
            });
        }
    }
}
