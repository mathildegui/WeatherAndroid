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
public class MainFragment extends Fragment implements Observer<List<Weather>> {

    @Bind(R.id.currentTemp) TextView mCurrentTemp;
    @Bind(R.id.currentHumidity) TextView mCurrentHumidity;

    TextView mCurrentT;

    private Weather mWeather;
    private Subscription mSubscription = Subscriptions.empty();

    public MainFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, v);

        mCurrentT = (TextView)v.findViewById(R.id.currentTemp);

        /**
         * FIXME load only the last value and not the entire list of data
         */
        mSubscription =
                AndroidObservable
                        .fromFragment(this, ApiManager.loadW())
                        .observeOn(Schedulers.threadPoolForIO())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(this);

        mCurrentT.setText(String.valueOf("value"));
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
                    mCurrentTemp.setText(String.valueOf(mWeather.temperature));
                    mCurrentHumidity.setText(String.valueOf(mWeather.humidity));
                }
            });
        }
    }
}
