package com.mathildeguillossou.weathersensor.api;

import com.mathildeguillossou.weathersensor.bean.Weather;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;

/**
 * Created by mathildeguillossou on 03/12/15.
 */
public class ApiManager {
    public final static String TAG     = "ApiManager";
    public static final String API_URL = "http://192.168.1.30:3000/";

    public static ApiManagerService apiManagerServiceInterface;
    private ApiClassInterface.ApiListInterface apiInterface;

    public ApiManager(ApiClassInterface.ApiListInterface apiInterface) {
        apiManagerServiceInterface = getApiClient();
        this.apiInterface          = apiInterface;
    }

    public static ApiManagerService getApiClient() {
        //if(apiManagerServiceInterface != null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiManagerServiceInterface = retrofit.create(ApiManagerService.class);
        //}
        return apiManagerServiceInterface;
    }

    private interface ApiManagerService {
        @GET("/weathers")
        Call<List<Weather>> getWeathers();
    }

    public void loadW() {
        Call<List<Weather>> call = apiManagerServiceInterface.getWeathers();

        call.enqueue(new Callback<List<Weather>>() {
            @Override
            public void onResponse(Response<List<Weather>> response, Retrofit retrofit) {
                List<Weather> weathers = response.body();
                apiInterface.success(weathers);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}