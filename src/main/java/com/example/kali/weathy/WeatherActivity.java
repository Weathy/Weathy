package com.example.kali.weathy;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.kali.weathy.adaptors.TwentyFourListAdaptor;
import com.example.kali.weathy.adaptors.WeatherPagerAdapter;
import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;


public class WeatherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ViewPager vPager;
    private Button menuButton;
    private Button searchButton;
    private WeatherPagerAdapter adapter;
    private MyInnerReceiver receiver;
    private FirstQueryReceiver firstQueryReceiver;
    private PullRefreshLayout layout;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        updateWidgets();

        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isNetworkAvailable()) {
                    Toast.makeText(WeatherActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(WeatherActivity.this, RequestWeatherIntentService.class);
                    String city = DBManager.getInstance(WeatherActivity.this).getLastWeather().getCityName().split(",")[0];
                    String country = DBManager.getInstance(WeatherActivity.this).getLastWeather().getCityName().split(",")[1];
                    intent.putExtra("city", city);
                    intent.putExtra("country", country.trim());
                    startService(intent);
                }
            }
        });

        receiver = new MyInnerReceiver();
        registerReceiver(receiver, new IntentFilter("SerciveComplete"));

        firstQueryReceiver = new FirstQueryReceiver();
        registerReceiver(firstQueryReceiver, new IntentFilter("FirstQueryComplete"));

        TextView cityNameTV = (TextView) findViewById(R.id.city_name_textview);
        cityNameTV.setText(DBManager.getInstance(this).getLastWeather().getCityName());
        TextView lastUpdateTV = (TextView) findViewById(R.id.renewed_textview);
        lastUpdateTV.setText(DBManager.getInstance(this).getLastWeather().getLastUpdate());
        adapter = new WeatherPagerAdapter(getSupportFragmentManager());

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        vPager = (ViewPager) findViewById(R.id.view_pager);
        vPager.setAdapter(adapter);
        PageListener pageListener = new PageListener();
        vPager.setOnPageChangeListener(pageListener);

        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, SearchActivity.class);
                intent.putExtra("condition", DBManager.getInstance(WeatherActivity.this).getLastWeather().getDescription());
                startActivityForResult(intent, 200);
            }
        });
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        menuButton = (Button) findViewById(R.id.drawer_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        switch (DBManager.getInstance(this).getLastWeather().getDescription()) {
            case "Clear":
                if (hour >= 20 || hour <= 7) {
                    findViewById(R.id.content).setBackgroundResource(R.drawable.night_clear);
                    break;
                }
                findViewById(R.id.content).setBackgroundResource(R.drawable.day_clear);
                break;
            case "Clouds":
                if (hour >= 20 || hour <= 7) {
                    findViewById(R.id.content).setBackgroundResource(R.drawable.night_cloudy);
                    break;
                }
                findViewById(R.id.content).setBackgroundResource(R.drawable.day_cloudy);
                break;
            case "Thunderstorm":
                if (hour >= 20 || hour <= 7) {
                    findViewById(R.id.content).setBackgroundResource(R.drawable.night_thunderstorm);
                    break;
                }
                findViewById(R.id.content).setBackgroundResource(R.drawable.day_thunderstorm);
                break;
            case "Rain":
                if (hour >= 20 || hour <= 7) {
                    findViewById(R.id.content).setBackgroundResource(R.drawable.night_rain);
                    break;
                }
                findViewById(R.id.content).setBackgroundResource(R.drawable.day_rain);
                break;
        }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        } else if (id == R.id.last_searches) {
            FragmentManager fm = getSupportFragmentManager();
            DialogFragment newFragment = new LastSearchDialogFragment();
            newFragment.show(fm, "lastSearchDialog");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
                unregisterReceiver(firstQueryReceiver);
            } catch (IllegalArgumentException e) {

            }
        }
        super.onDestroy();
    }

    private void updateWidgets() {
        Intent intent = new Intent(this, Widget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), Widget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Weather Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class MyInnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            vPager.getAdapter().notifyDataSetChanged();
            layout.setRefreshing(false);
        }
    }

    class FirstQueryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            WeatherActivity.this.recreate();
            if (LastSearchDialogFragment.lastSearchDialog != null) {
                LastSearchDialogFragment.lastSearchDialog.dismiss();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        WeatherActivity.this.recreate();
    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener{

        @Override
        public void onPageSelected(int position) {
            if(position==1){
                TwentyFourFragment fragment = (TwentyFourFragment) adapter.getItem(position);
                if(fragment.adaptor != null){
                    fragment.adaptor = new TwentyFourListAdaptor(WeatherActivity.this, DBManager.getInstance(WeatherActivity.this).getTwentyHourForecastObjects());
                }
            }
        }
    }
}
