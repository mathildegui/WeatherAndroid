package com.mathildeguillossou.weathersensor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.adapter.MyWeatherRecyclerViewAdapter;
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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WeatherListFragment extends Fragment implements Observer<List<Weather>> {

    private MyWeatherRecyclerViewAdapter mAdapter;
    private List<Weather> mWeatherList = new ArrayList<>();
    private Subscription mSubscription = Subscriptions.empty();

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeatherListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WeatherListFragment newInstance(int columnCount) {
        WeatherListFragment fragment = new WeatherListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_list, container, false);

        mAdapter = new MyWeatherRecyclerViewAdapter(mWeatherList, mListener);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mAdapter);
        }

        mSubscription =
                AndroidObservable
                        .fromFragment(this, ApiManager.loadW())
                        .observeOn(Schedulers.threadPoolForIO())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(this);
        return view;
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
        e.printStackTrace();
    }

    @Override
    public void onNext(List<Weather> args) {
        mWeatherList.clear();
        mWeatherList.addAll(args);
        new ReceiverThread().run();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Weather item);
    }

    /**
     * Because only the original thread that created a view hierarchy can touch its views
     */
    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            WeatherListFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
