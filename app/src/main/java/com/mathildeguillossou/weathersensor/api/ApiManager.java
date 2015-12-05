package com.mathildeguillossou.weathersensor.api;

import android.util.Log;

import com.mathildeguillossou.weathersensor.bean.Weather;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by mathildeguillossou on 03/12/15.
 */
public class ApiManager {
    public final static String TAG     = "ApiManager";
    //TODO: change to ip box
    public static final String API_URL = "http://maelgui.fr:3000/";

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static ApiManagerService apiManagerServiceInterface = retrofit.create(ApiManagerService.class);

    private interface ApiManagerService {
        @GET("/weathers")
        Call<List<Weather>> getWeathers();
    }

    public static Observable<List<Weather>> loadW() {
        return Observable.create(new Observable.OnSubscribeFunc<List<Weather>>() {
            @Override
            public Subscription onSubscribe(Observer<? super List<Weather>> observer) {
                final Observer<? super List<Weather>> internalObserver = observer;
                Call<List<Weather>> call = apiManagerServiceInterface.getWeathers();
                call.enqueue(new Callback<List<Weather>>() {
                    @Override
                    public void onResponse(Response<List<Weather>> response, Retrofit retrofit) {
                        List<Weather> weathers = response.body();
                        try {
                            internalObserver.onNext(weathers);
                            internalObserver.onCompleted();
                        } catch (Exception e) {
                            internalObserver.onError(e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("onFailure", "onFailure");
                    }
                });
                return Subscriptions.empty();
                /*return new Subscription() {
                    @Override
                    public void unsubscribe() {
                        // code à exécuter lorsqu'un Observer se désinscrit
                    }
                }*/
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}