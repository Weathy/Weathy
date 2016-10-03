package com.example.kali.weathy;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kali.weathy.Database.DBManager;
import com.example.kali.weathy.Database.RequestTask;
import com.example.kali.weathy.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

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
        new TwentyFourTask(this).execute();
        new RequestTask(this).execute();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        vPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new WeatherPagerAdapter(getSupportFragmentManager());
        vPager.setAdapter(adapter);
    }


    public class WeatherPagerAdapter extends FragmentPagerAdapter {


        private final static int NUMBER_OF_FRAGMENTS = 3;

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
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.remove(fm.findFragmentById(R.id.city_forecast));
            fragmentTransaction.add(R.id.drawer_layout,new SearchFragment(),"Search fragment");
            fragmentTransaction.commit();
            return true;
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

    class TwentyFourTask extends AsyncTask<Void, Void, ArrayList<Weather.TwentyFourWeather>> {
        private Activity context;
        private StringBuilder twentyFourJSON = new StringBuilder();
        private int currentTemp;
        private int feelsLike;
        private double windSpeed;
        private int humidity;
        private String condition;
        private int airPressure;
        private String time;
        private String iconURL;
        private String date;
        private ArrayList<Weather.TwentyFourWeather> list = new ArrayList<>();

        public TwentyFourTask(Activity context){
            this.context = context;
        }
        @Override
        protected ArrayList<Weather.TwentyFourWeather> doInBackground(Void... params) {
            DBManager.getInstance(context).getWritableDatabase().execSQL("delete from twenty_hour_forecast");
            try {
                URL twentyFourInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/hourly/q/sofia.json");
                HttpURLConnection connection = (HttpURLConnection) twentyFourInfo.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStr = connection.getInputStream();
                Scanner twentyFourScanner = new Scanner(inputStr);
                while(twentyFourScanner.hasNextLine()){
                    twentyFourJSON.append(twentyFourScanner.nextLine());
                }

                JSONObject obj = new JSONObject(twentyFourJSON.toString());
                JSONArray twentyFourArray = obj.getJSONArray("hourly_forecast");
                for(int i = 0; i<24; i++){
                    JSONObject obj1 = new JSONObject(twentyFourArray.getJSONObject(i).toString());

                    currentTemp = obj1.getJSONObject("temp").getInt("metric");
                    feelsLike = obj1.getJSONObject("feelslike").getInt("metric");
                    windSpeed = obj1.getJSONObject("wspd").getDouble("metric");
                    humidity = obj1.getInt("humidity");
                    condition= obj1.getString("condition");
                    airPressure = obj1.getJSONObject("mslp").getInt("metric");
                    time = obj1.getJSONObject("FCTTIME").getString("hour")+":"+obj1.getJSONObject("FCTTIME").getString("min");
                    iconURL = obj1.getString("icon_url");
                    date = obj1.getJSONObject("FCTTIME").getString("weekday_name") + ", " + obj1.getJSONObject("FCTTIME").getString("mday") + "." + obj1.getJSONObject("FCTTIME").getString("month_name") + "." + obj1.getJSONObject("FCTTIME").getString("year");
                    list.add(new Weather.TwentyFourWeather(currentTemp, iconURL, feelsLike, Double.toString(windSpeed), humidity, condition, airPressure, time, date));
                    DBManager.getInstance(context).addTwentyHourWeather(currentTemp,feelsLike,windSpeed+"",humidity,condition,airPressure,time,date);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return list;
        }
    }

    @Override
    public ArrayList<Weather.TwentyFourWeather> getData() {
        return twentyFourHourForecast;
    }

}
