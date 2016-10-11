package com.example.kali.weathy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.kali.weathy.adaptors.TenDayListAdaptor;
import com.example.kali.weathy.adaptors.TwentyFourListAdaptor;
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
    private  MyInnerReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        receiver = new MyInnerReceiver();
        registerReceiver(receiver,new IntentFilter("SerciveComplete"));

        adapter = new WeatherPagerAdapter(getSupportFragmentManager());

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

    @Override
    protected void onDestroy() {
        if(receiver != null){
            try {
                unregisterReceiver(receiver);
            }
            catch (IllegalArgumentException e){

            }
        }
        super.onDestroy();
    }

    class MyInnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            TwentyFourFragment twentyFourFragment = (TwentyFourFragment) adapter.getItem(1);
            TenDayFragment tenDayFragment = (TenDayFragment) adapter.getItem(2);

            twentyFourFragment.adaptor = new TwentyFourListAdaptor(WeatherActivity.this, DBManager.getInstance(WeatherActivity.this).getTwentyHourForecastObjects());
            twentyFourFragment.adaptor.notifyDataSetChanged();

            tenDayFragment.adaptor = new TenDayListAdaptor(WeatherActivity.this, DBManager.getInstance(WeatherActivity.this).getTenDayForecast());
            tenDayFragment.adaptor.notifyDataSetChanged();
        }
    }


}
