package com.example.kali.weathy;

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
import android.widget.Toast;

import com.example.kali.weathy.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WeatherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TenDayFragment.TenDayComunicator,
        TwentyFourFragment.TwentyFourComunicator, CityForecastFragment.CityForecastComunicator {

    private WeatherPagerAdapter adapter;


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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager vPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new WeatherPagerAdapter(getSupportFragmentManager());
        vPager.setAdapter(adapter);
    }


    private class WeatherPagerAdapter extends FragmentPagerAdapter {

        private final static int NUMBER_OF_FRAGMENTS = 3;

        WeatherPagerAdapter(FragmentManager fm) {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        StringBuilder coordinatesJSON = new StringBuilder();
        StringBuilder weatherJSON = new StringBuilder();
        String lat;
        String lng;
        String cityName;
        @Override
        protected Weather doInBackground(Void... params) {
            try {
                URL coordinates = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=София&key=AIzaSyCmuqUqYeQXBzTT4qpKPBI5uhSLW1m-l2I");
                Log.e("url", coordinates.toString());
                HttpURLConnection connection = (HttpURLConnection) coordinates.openConnection();
                connection.setRequestMethod("GET");
                InputStream stream = connection.getInputStream();
                Scanner sc = new Scanner(stream);
                while (sc.hasNextLine()) {
                    coordinatesJSON.append(sc.nextLine());
                }
                JSONObject weatherObj = new JSONObject(coordinatesJSON.toString());
                JSONArray array = weatherObj.getJSONArray("results");
                JSONObject resultInArr = array.getJSONObject(0);
                cityName = resultInArr.getString("formatted_address");
                lat = resultInArr.getJSONObject("geometry").getJSONObject("location").getString("lat");
                lng = resultInArr.getJSONObject("geometry").getJSONObject("location").getString("lng");
                sc.close();
                stream.close();

                URL weatherInfo = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lng+"&appid=9d01db38e0b771b0eb2fffa9e3640dd9");
                HttpURLConnection weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
                weatherConnection.setRequestMethod("GET");
                InputStream weatherStream = weatherConnection.getInputStream();
                Scanner weatherScanner = new Scanner(weatherStream);
                while (weatherScanner.hasNextLine()) {
                    weatherJSON.append(weatherScanner.nextLine());
                }
                JSONObject weather = new JSONObject(weatherJSON.toString());
                //cityName = weather.getString("name");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new Weather(cityName,0,0,0,null,null,null,null,0,0,0,0);
        }

        @Override
        protected void onPostExecute(Weather weather) {
            Log.e("weather", weatherJSON.toString());
            Log.e("weather", "lat:" + lat + " lng" + lng);
            if(weather!=null)
                Toast.makeText(WeatherActivity.this, weather.getCityName() , Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(WeatherActivity.this, "no weather", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
