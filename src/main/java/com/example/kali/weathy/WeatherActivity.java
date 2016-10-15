package com.example.kali.weathy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.kali.weathy.adaptors.TenDayListAdaptor;
import com.example.kali.weathy.adaptors.TwentyFourListAdaptor;
import com.example.kali.weathy.adaptors.WeatherPagerAdapter;
import com.example.kali.weathy.database.AlarmReceiver;
import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.DeviceBootReceiver;
import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.example.kali.weathy.model.Weather;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ViewPager vPager;
    private Button searchButton;
    private Button refreshButton;
    private WeatherPagerAdapter adapter;
    private  MyInnerReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        updateWidgets(this);

        receiver = new MyInnerReceiver();
        registerReceiver(receiver,new IntentFilter("SerciveComplete"));


        TextView cityNameTV = (TextView) findViewById(R.id.city_name_textview);
        cityNameTV.setText(DBManager.getInstance(this).getLastWeather().getCityName());
        TextView lastUpdateTV = (TextView) findViewById(R.id.renewed_textview);
        lastUpdateTV.setText(DBManager.getInstance(this).getLastWeather().getLastUpdate());

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

        refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, RequestWeatherIntentService.class);
                String city = DBManager.getInstance(WeatherActivity.this).getLastWeather().getCityName().split(",")[0];
                String country = DBManager.getInstance(WeatherActivity.this).getLastWeather().getCityName().split(",")[1];
                intent.putExtra("city", city);
                intent.putExtra("country", country.trim());
                startService(intent);
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
            vPager.setCurrentItem(0, true);
        } else if (id == R.id.twenty_four_item) {
            vPager.setCurrentItem(1, true);
        } else if (id == R.id.ten_day_item) {
            vPager.setCurrentItem(2, true);
        } else if (id == R.id.serch_item) {
            Intent intent = new Intent(WeatherActivity.this, SearchActivity.class);
            intent.putExtra("condition", DBManager.getInstance(WeatherActivity.this).getLastWeather().getDescription());
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void updateWidgets(Context context){
        Intent intent = new Intent(this, Widget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), Widget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
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

            DBManager.getInstance(WeatherActivity.this).addLastSearch();
            Log.e("update" , "update");
            //WeatherActivity.this.recreate();
        }
    }


}
