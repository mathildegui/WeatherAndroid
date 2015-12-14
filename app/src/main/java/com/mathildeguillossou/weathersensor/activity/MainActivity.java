package com.mathildeguillossou.weathersensor.activity;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.api.ApiManager;
import com.mathildeguillossou.weathersensor.bean.Weather;
import com.mathildeguillossou.weathersensor.fragment.ChartFragment;
import com.mathildeguillossou.weathersensor.fragment.MainFragment;
import com.mathildeguillossou.weathersensor.fragment.WeatherListFragment;
import com.mathildeguillossou.weathersensor.fragment.dialog.DialogAddFragment;
import com.mathildeguillossou.weathersensor.utils.Connection;
import com.mathildeguillossou.weathersensor.utils.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.android.observables.AndroidObservable;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

public class MainActivity extends AppCompatActivity implements DialogAddFragment.DialogListener,
        WeatherListFragment.OnListFragmentInteractionListener, Observer<Weather> {

    private FragmentManager mFragmentManager;
    private Subscription mSubAdd = Subscriptions.empty();

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Require the ACCESS_NETWORK_STATE permission
        boolean isConnected = Connection.checkActiveNetwork(getApplicationContext());

        mFragmentManager = getSupportFragmentManager();

        if(!isConnected) {
            setContentView(R.layout.no_network);
            findViewById(R.id.not_found).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, NotFoundActivity.class));
                }
            });
        } else {
            setContentView(R.layout.activity_main);
            setupFrag(savedInstanceState);
            setupFloatingButton();
        }
        initApp();
    }

    @Override
    public void onDestroy() {
        mSubAdd.unsubscribe();
        super.onDestroy();
    }

    private void initApp() {
        ButterKnife.bind(this);
        setupToolbar();
        setupDrawer();
        setDrawerContent();
    }

    private void setDrawerContent() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });

        ImageView ivHeader = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.nav_header);
        int resourceId = getResources().getIdentifier("rockymontains_" + Date.getTime(), "drawable", getPackageName());

        ivHeader.setBackgroundResource(resourceId);
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment f = null;
        Class fragClas;

        switch (item.getItemId()) {
            case R.id.nav_chart_fragment:
                fragClas = ChartFragment.class;
                break;
            case R.id.nav_list_fragment:
                fragClas = WeatherListFragment.class;
                break;
            default:
                fragClas = MainFragment.class;
        }

        try {
            f = (Fragment) fragClas.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, f)
                .commit();

        item.setChecked(true);
        setTitle(item.getTitle());
        mDrawerLayout.closeDrawers();
    }

    private void setupFrag(Bundle b) {
        if(b == null) {
            mFragmentManager
                    .beginTransaction()
                    .addToBackStack(MainFragment.class.getSimpleName())
                    .add(R.id.content_frame, new MainFragment())
                    .commit();
        }
    }

    private void setupFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DialogAddFragment();
                newFragment.show(getSupportFragmentManager(), "Add value");
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    private void setupDrawer() {
        ActionBarDrawerToggle abToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
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
    }

    private void setupToolbar() {
        if(mToolbar != null)
            setSupportActionBar(mToolbar);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onListFragmentInteraction(Weather item) {
        Log.d("onFragment Interaction", "from the act");
    }

    @Override
    public void onDialogPositiveClick(Float humidity, Float temperature) {
        Weather w = new Weather(humidity, temperature);
        mSubAdd =
                AndroidObservable
                        .fromActivity(MainActivity.this, ApiManager.addW(w))
                        .observeOn(Schedulers.threadPoolForIO())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(this);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onCompleted() {
        Log.d("on", "completed");
    }

    @Override
    public void onError(Throwable e) {
        Log.d("on", "erro");
    }

    @Override
    public void onNext(Weather args) {
        Log.d("on", "next");
    }
}
