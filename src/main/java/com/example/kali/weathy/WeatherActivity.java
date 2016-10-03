package com.example.kali.weathy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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

        new RequestTask().execute();
        new TwentyFourTask().execute();

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
            new RequestTask();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, new SearchFragment(), "Search fragment").commit();
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

    public void changeFragment(Fragment f) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.view_pager, new SearchFragment());
        adapter.notifyDataSetChanged();
        trans.commit();

    }

    class RequestTask extends AsyncTask<Void, Void, Weather> {
        private static final double KELVIN_CONSTANT = 272.15;
        private StringBuilder weatherJSON = new StringBuilder();
        private int currentTemp;
        private int feelsLike;
        private String lat;
        private String lng;
        private String cityName;
        private String description;
        private String icon;
        private int temp_min;
        private int temp_max;
        private String sunrise;
        private String sunset;
        private double windSpeed;
        private int humidity;
        private int pressure;
        private double visibility;
        private String lastUpdate;
        @Override
        protected Weather doInBackground(Void... params) {
            try {
                URL weatherInfo = new URL("http://api.openweathermap.org/data/2.5/weather?q=Sofia&appid=9d01db38e0b771b0eb2fffa9e3640dd9");
                HttpURLConnection weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
                weatherConnection.setRequestMethod("GET");
                InputStream weatherStream = weatherConnection.getInputStream();
                Scanner weatherScanner = new Scanner(weatherStream);
                while (weatherScanner.hasNextLine()) {
                    weatherJSON.append(weatherScanner.nextLine());
                }
                Log.e("error",weatherJSON.toString());

                JSONObject weather = new JSONObject(weatherJSON.toString());
                description = weather.getJSONArray("weather").getJSONObject(0).getString("main");
                icon = weather.getJSONArray("weather").getJSONObject(0).getString("icon");
                temp_min = (int)(weather.getJSONObject("main").getInt("temp_min")-KELVIN_CONSTANT);
                temp_max = (int)(weather.getJSONObject("main").getInt("temp_max")-KELVIN_CONSTANT);
                windSpeed = weather.getJSONObject("wind").getDouble("speed");
                humidity = weather.getJSONObject("main").getInt("humidity");
                pressure = weather.getJSONObject("main").getInt("pressure");
                currentTemp = (int)(weather.getJSONObject("main").getInt("temp")-KELVIN_CONSTANT);
                weatherJSON.delete(0,weatherJSON.length());
                weatherInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/conditions/q/Bulgaria/Sofia.json");
                weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
                weatherConnection.setRequestMethod("GET");
                weatherStream = weatherConnection.getInputStream();
                weatherScanner = new Scanner(weatherStream);
                while (weatherScanner.hasNextLine()) {
                    weatherJSON.append(weatherScanner.nextLine());
                }
                weather = new JSONObject(weatherJSON.toString());
                feelsLike = weather.getJSONObject("current_observation").getInt("feelslike_c");
                String [] up =  weather.getJSONObject("current_observation").getString("local_time_rfc822").split("[+]");
                lastUpdate = up[0];
                cityName = weather.getJSONObject("current_observation").getJSONObject("display_location").getString("full");
                weatherJSON.delete(0,weatherJSON.length());
                weatherInfo = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22Sofia%2C%20bg%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
                weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
                weatherConnection.setRequestMethod("GET");
                weatherStream = weatherConnection.getInputStream();
                weatherScanner = new Scanner(weatherStream);
                while (weatherScanner.hasNextLine()) {
                    weatherJSON.append(weatherScanner.nextLine());
                }
                weather = new JSONObject(weatherJSON.toString());
                visibility = weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getDouble("visibility");
                sunset = weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunset");
                sunrise = weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunrise");



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new Weather(cityName,currentTemp,feelsLike,humidity,windSpeed,pressure,sunrise,sunset,description,icon,temp_min,temp_max,lat,lng,visibility,lastUpdate);
        }
        @Override
        protected void onPostExecute(Weather weather) {
            Log.e("weather" , weather.toString());
            TextView cityNameTV = (TextView) findViewById(R.id.city_name_textview);
            cityNameTV.setText(weather.getCityName());
            TextView visibilityTV = (TextView) findViewById(R.id.visibility_textview);
            visibilityTV.setText(weather.getVisibility()+"");
            TextView lastUpdateTV = (TextView) findViewById(R.id.renewed_textview);
            lastUpdateTV.setText(weather.getLastUpdate());
            TextView sunsetTV = (TextView) findViewById(R.id.time_sunset_textview);
            sunsetTV.setText(weather.getSunset());
            TextView feelsLikeTV = (TextView) findViewById(R.id.temperature_status_textview);
            feelsLikeTV.setText(weather.getFeelsLike()+"");
            TextView statusTV = (TextView)findViewById(R.id.weather_status_textview);
            statusTV.setText(weather.getDescription());
            TextView degreeTV = (TextView)findViewById(R.id.degrees_tv);
            degreeTV.setText(weather.getCurrentTemp()+"");
            TextView airPresure = (TextView)findViewById(R.id.air_pressure_textview);
            airPresure.setText(weather.getPressure()+" HPa");
            TextView humidity = (TextView)findViewById(R.id.humidity_textview);
            humidity.setText(weather.getHumidity()+"%");
            TextView wind = (TextView)findViewById(R.id.wind_meter_in_second_textview);
            wind.setText(weather.getWindSpeed()+"");
            TextView cityName = (TextView) findViewById(R.id.city_name_textview);
            cityName.setText(weather.getCityName()+"");
            TextView feelsLike = (TextView) findViewById(R.id.temperature_status_textview);
            feelsLike.setText(weather.getFeelsLike()+"");
            switch (weather.getDescription()){
                case "Clear" :
                    findViewById(R.id.content).setBackgroundResource(R.drawable.day_clear);
                    return;
                case "Clouds" :
                    findViewById(R.id.content).setBackgroundResource(R.drawable.day_cloudy);
                    return;
                case "Thunderstorm" :
                    findViewById(R.id.content).setBackgroundResource(R.drawable.day_thunderstorm);
                    return;
                case "Rain" :
                    findViewById(R.id.content).setBackgroundResource(R.drawable.rain);
                    return;
            }

        }


    }

    class TwentyFourTask extends AsyncTask<Void, Void, ArrayList<Weather.TwentyFourWeather>> {

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
        @Override
        protected ArrayList<Weather.TwentyFourWeather> doInBackground(Void... params) {
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
                    list.add(new Weather.TwentyFourWeather(currentTemp, iconURL, feelsLike, windSpeed, humidity, condition, airPressure, time, date));
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

        @Override
        protected void onPostExecute(ArrayList<Weather.TwentyFourWeather> list) {

            TwentyFourFragment fr = (TwentyFourFragment) WeatherActivity.this.getSupportFragmentManager().findFragmentById(R.id.twenty_four_fragment);
            if(fr != null){
                fr.refreshAdaptor(list);
            }
        }
    }

    @Override
    public ArrayList<Weather.TwentyFourWeather> getData() {
        return twentyFourHourForecast;
    }

}
