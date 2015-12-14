package com.mathildeguillossou.weathersensor.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.api.ApiManager;
import com.mathildeguillossou.weathersensor.bean.Weather;
import com.mathildeguillossou.weathersensor.utils.Number;

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
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private Subscription mSubsGetLast = Subscriptions.empty();

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, v);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                subscribe();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        subscribe();
        return v;
    }

    private void subscribe() {
        mSubsGetLast =
                AndroidObservable
                        .fromFragment(this, ApiManager.getLast())
                        .observeOn(Schedulers.threadPoolForIO())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(this);
    }

    private void unsubscribe() {
        mSubsGetLast.unsubscribe();
    }

    @Override
    public void onDestroy() {
        unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Because after swipe, when click on the left menu,
        // this fragment remain and the other is above
        if (swipeContainer!=null) {
            swipeContainer.setRefreshing(false);
            swipeContainer.destroyDrawingCache();
            swipeContainer.clearAnimation();
        }
        unsubscribe();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Weather w) {
        updateFields(w.temperature, w.humidity);
    }

    public void updateFields(float temp, float hum) {
        new ReceiverThread(temp, hum).run();
    }

    /**
     * Because only the original thread that created a view hierarchy can touch its views
     */
    public class ReceiverThread extends Thread {

        private float hum;
        private float temp;

        public ReceiverThread(float temp, float hum) {
            this.hum  = hum;
            this.temp = temp;
        }

        @Override
        public void run() {
            MainFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String t = Number.isNullDecimal(temp)?
                            String.valueOf(temp):
                            String.format("%.2f", temp);
                    mCurrentTemp.setText(t);
                    String h = Number.isNullDecimal(hum)?
                            String.valueOf(hum):
                            String.format("%.2f", hum);
                    mCurrentHumidity.setText(h);
                    if(swipeContainer != null) swipeContainer.setRefreshing(false);
                }
            });
        }
    }
}
