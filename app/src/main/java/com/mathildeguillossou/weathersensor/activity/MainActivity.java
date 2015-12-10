package com.mathildeguillossou.weathersensor.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.bean.Weather;
import com.mathildeguillossou.weathersensor.fragment.ChartFragment;
import com.mathildeguillossou.weathersensor.fragment.MainFragment;
import com.mathildeguillossou.weathersensor.fragment.WeatherListFragment;

public class MainActivity extends AppCompatActivity implements WeatherListFragment.OnListFragmentInteractionListener, View.OnClickListener {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_drawer);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .addToBackStack(MainFragment.class.getSimpleName())
                    .add(R.id.content_frame, new MainFragment())
                    .commit();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.findViewById(R.id.list).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.chart).setOnClickListener(this);

        ActionBarDrawerToggle abToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                mDrawerLayout.bringToFront();
            }

            public void onDrawerSlide(View drawerView, float slideOffset) {

            }
        };
        mDrawerLayout.setDrawerListener(abToggle);

        //Floating bottom button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onListFragmentInteraction(Weather item) {
        Log.d("onFragment Interaction", "from the act");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chart:
                Log.d("test", "test");
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(MainFragment.class.getSimpleName())
                        .replace(R.id.content_frame, ChartFragment.newInstance())
                        .commit();
                mDrawerLayout.closeDrawers();
                break;
        }
    }
}
