package com.mathildeguillossou.weathersensor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.bean.Weather;
import com.mathildeguillossou.weathersensor.fragment.WeatherListFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Weather} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyWeatherRecyclerViewAdapter extends RecyclerView.Adapter<MyWeatherRecyclerViewAdapter.ViewHolder> {

    private final List<Weather> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyWeatherRecyclerViewAdapter(List<Weather> items, OnListFragmentInteractionListener listener) {
        mValues   = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mHumidityView.setText(String.valueOf(mValues.get(position).humidity));
        holder.mTemperatureView.setText(String.valueOf(mValues.get(position).temperature));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Weather mItem;
        public final View mView;
        public final TextView mHumidityView;
        public final TextView mTemperatureView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHumidityView    = (TextView) view.findViewById(R.id.humidity);
            mTemperatureView = (TextView) view.findViewById(R.id.temperature);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mHumidityView.getText() + "'";
        }
    }
}
