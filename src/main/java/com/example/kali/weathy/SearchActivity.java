package com.example.kali.weathy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class SearchActivity extends AppCompatActivity implements PlaceSelectionListener{
    private String cityName;
    private Intent intent;
    private ProgressBar progressBar;
    private MyInnerReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        receiver = new MyInnerReceiver();
        registerReceiver(receiver,new IntentFilter("SerciveComplete"));
        progressBar = (ProgressBar) findViewById(R.id.progress_search);
        PlaceAutocompleteFragment fragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_fragment);
        fragment.setOnPlaceSelectedListener(this);
        fragment.setHint("Search for location");
        AutocompleteFilter filter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        fragment.setFilter(filter);
        String condition = getIntent().getStringExtra("condition");

        if(condition != null) {
            switch (condition) {
                case "Clear":
                    findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_clear);
                    break;
                case "Clouds":
                    findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_cloudy);
                    break;
                case "Thunderstorm":
                    findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_thunderstorm);
                    break;
                case "Rain":
                    findViewById(R.id.activity_search).setBackgroundResource(R.drawable.rain);
                    break;
            }
        }
        else{
            findViewById(R.id.activity_search).setBackgroundResource(R.drawable.loading_screen_background);
        }


    }

    @Override
    public void onPlaceSelected(Place place) {
        progressBar.setVisibility(View.VISIBLE);
        cityName = place.getName().toString();
        intent = new Intent(this, RequestWeatherIntentService.class);
        intent.putExtra("city", cityName);
        startService(intent);
    }

    @Override
    public void onError(Status status) {

        Toast.makeText(this, "You did something stupid", Toast.LENGTH_SHORT).show();

    }


    class MyInnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context,WeatherActivity.class);
            startActivity(intent1);
        }
    }
}
