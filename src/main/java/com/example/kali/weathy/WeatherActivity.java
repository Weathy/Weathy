package com.example.kali.weathy;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.RequestTask;
import com.example.kali.weathy.database.TenDayTask;
import com.example.kali.weathy.database.TwentyFourTask;
import com.example.kali.weathy.model.Weather;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TenDayFragment.TenDayComunicator,
        TwentyFourFragment.TwentyFourComunicator, CityForecastFragment.CityForecastComunicator {

    private WeatherPagerAdapter adapter;
    private ViewPager vPager;
    public ArrayList<Weather.TwentyFourWeather> twentyFourHourForecast = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//        new TwentyFourTask(this).execute();
//        new TenDayTask(this).execute();
//        if(DBManager.getInstance(this).getLastWeather()==null) {
//            new RequestTask(this).execute("Sofia");
//        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        vPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new WeatherPagerAdapter(getSupportFragmentManager());
        vPager.setAdapter(adapter);
    }


    public class WeatherPagerAdapter extends FragmentPagerAdapter {


        private final static int NUMBER_OF_FRAGMENTS = 4;

        public WeatherPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CityForecastFragment.newInstance("one");
                case 1:
                    return TwentyFourFragment.newInstance("two");
                case 2:
                    return TenDayFragment.newInstance("three");
                case 3:
                    return SearchFragment.newInstance("four");
                default:
                    return CityForecastFragment.newInstance("one");

            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_FRAGMENTS;
        }
    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().findFragmentByTag("Search fragment") != null){
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Search fragment")).commit();
            vPager.setAdapter(new WeatherPagerAdapter(getSupportFragmentManager()));
        }
        else{
            super.onBackPressed();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
//            SearchFragment fragment = new SearchFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new SearchFragment(),"Search fragment").commit();
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
