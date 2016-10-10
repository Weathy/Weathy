package com.example.kali.weathy;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.kali.weathy.adaptors.WeatherPagerAdapter;
import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.model.Weather;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TenDayFragment.TenDayComunicator,
        TwentyFourFragment.TwentyFourComunicator, CityForecastFragment.CityForecastComunicator {

    public ViewPager vPager;
    public ArrayList<Weather.TwentyFourWeather> twentyFourHourForecast = new ArrayList<>();
    private Button searchButton;
    private WeatherPagerAdapter adapter;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFragments();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        adapter = new WeatherPagerAdapter(getSupportFragmentManager());

        String intent = getIntent().getStringExtra("refresh");
        if(intent != null && !intent.isEmpty()){
            updateFragments();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        vPager = (ViewPager) findViewById(R.id.view_pager);
        vPager.setAdapter(adapter);

        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButton.setBackgroundResource(R.drawable.button_clicked);
                Intent intent = new Intent(WeatherActivity.this, SearchActivity.class);
                intent.putExtra("condition", DBManager.getInstance(WeatherActivity.this).getLastWeather().getDescription());
                startActivity(intent);
                finish();
            }
        });

    }

    public void updateFragments() {
        for (int i = 0; i < adapter.getCount(); i++) {
            Log.e("fragments", "count" + adapter.getCount());
            Fragment fragment = adapter.getRegisteredFragment(i);
            if (fragment != null) {

                Log.e("fragments", "2");
                if (fragment instanceof TenDayFragment) {

                    Log.e("fragments", "3");
                    TenDayFragment f = (TenDayFragment) fragment;
                    if (f.adaptor != null) {
                        f.adaptor.notifyDataSetChanged();
                    }
                }
                if (fragment instanceof TwentyFourFragment) {

                    Log.e("fragments", "4");
                    TwentyFourFragment f = (TwentyFourFragment) fragment;
                    if (f.adaptor != null) {
                        f.adaptor.notifyDataSetChanged();
                    }
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.in_the_moment_item) {
        } else if (id == R.id.twenty_four_item) {
        } else if (id == R.id.ten_day_item) {
        } else if (id == R.id.serch_item) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public ArrayList<Weather.TwentyFourWeather> getData() {
        return twentyFourHourForecast;
    }


}
