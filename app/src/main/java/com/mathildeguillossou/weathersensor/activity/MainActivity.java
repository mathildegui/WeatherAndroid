package com.mathildeguillossou.weathersensor.activity;


import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mathildeguillossou.weathersensor.R;
import com.mathildeguillossou.weathersensor.api.ApiManager;
import com.mathildeguillossou.weathersensor.bean.Weather;
import com.mathildeguillossou.weathersensor.fragment.ChartFragment;
import com.mathildeguillossou.weathersensor.fragment.MainFragment;
import com.mathildeguillossou.weathersensor.fragment.SettingsFragment;
import com.mathildeguillossou.weathersensor.fragment.WeatherListFragment;
import com.mathildeguillossou.weathersensor.fragment.dialog.DialogAddFragment;
import com.mathildeguillossou.weathersensor.utils.Android;
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

public class MainActivity extends AppCompatActivity implements
        Observer<Weather>,
        DialogAddFragment.DialogListener,
        WeatherListFragment.OnListFragmentInteractionListener {

    //Keep the last failed weather to retry in case the user wants
    private Weather lastFailedW = null;

    private boolean isConnected;
    private FragmentManager mFragmentManager;
    private Subscription mSubAdd = Subscriptions.empty();

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Require the ACCESS_NETWORK_STATE permission
        isConnected = Connection.checkActiveNetwork(getApplicationContext());

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
            if(savedInstanceState == null) {
                Android.loadFragment(mFragmentManager,
                        MainFragment.class,
                        R.id.content_frame,
                        mFragmentManager.findFragmentById(R.id.content_frame) != null,
                        true);
            }
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
        if(!isConnected) {
            navigationView.getMenu().findItem(R.id.nav_list_fragment).setEnabled(false);
            navigationView.getMenu().findItem(R.id.nav_chart_fragment).setEnabled(false);
        }
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
        Class fragClas;

        switch (item.getItemId()) {
            case R.id.nav_chart_fragment:
                fragClas = ChartFragment.class;
                break;
            case R.id.nav_list_fragment:
                fragClas = WeatherListFragment.class;
                break;
            case R.id.nav_settings_fragment:
                fragClas = SettingsFragment.class;
                break;
            default:
                fragClas = MainFragment.class;
        }
        Android.loadFragment(mFragmentManager,
                fragClas,
                R.id.content_frame,
                mFragmentManager.findFragmentById(R.id.content_frame) != null,
                false);

        if(!isConnected && findViewById(R.id.not_found) != null)
            findViewById(R.id.not_found).setVisibility(View.GONE);

        item.setChecked(true);
        setTitle(item.getTitle());
        mDrawerLayout.closeDrawers();
    }

    private void setupFloatingButton() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DialogAddFragment();
                newFragment.show(getSupportFragmentManager(), "Add value");
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
    public void onDialogPositiveClick(Float humidity, Float temperature) {
        lastFailedW = new Weather(humidity, temperature);
        subscribe(lastFailedW);
    }

    private void subscribe(Weather w) {
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

    }

    @Override
    public void onError(Throwable e) {
         Snackbar sb = Snackbar.make(findViewById(R.id.coord_layout), R.string.smt_wrong_happened, Snackbar.LENGTH_LONG)
                .setAction(R.string.tryagain, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subscribe(lastFailedW);
                    }
                }).setActionTextColor(Color.RED);
        View sbView = sb.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        sb.show();
    }

    @Override
    public void onNext(Weather w) {
        lastFailedW = null;
        MainFragment f = (MainFragment) getSupportFragmentManager().
                findFragmentById(R.id.content_frame);
        f.updateFields(w.temperature, w.humidity);
    }

    @Override
    public void onListFragmentInteraction(Weather item) {

    }
}
