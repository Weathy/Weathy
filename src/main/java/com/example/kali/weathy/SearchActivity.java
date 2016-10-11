package com.example.kali.weathy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.example.kali.weathy.model.CityRequestListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SearchActivity extends AppCompatActivity implements PlaceSelectionListener{
    private String cityName;
    private String country;
    private Intent intent;
    private ProgressBar progressBar;
    private SearchReceiver receiver;
    private Button sofiaButton;
    private Button plovdivButton;
    private Button varnaButton;
    private Button burgasButton;
    private Button plevenButton;
    private Button ruseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        receiver = new SearchReceiver();
        registerReceiver(receiver,new IntentFilter("SerciveComplete"));
        sofiaButton = (Button) findViewById(R.id.sofia_button);
        plovdivButton = (Button) findViewById(R.id.plovdiv_button);
        varnaButton = (Button) findViewById(R.id.varna_button);
        burgasButton = (Button) findViewById(R.id.burgas_button);
        plevenButton = (Button) findViewById(R.id.pleven_button);
        ruseButton = (Button) findViewById(R.id.ruse_button);

        sofiaButton.setOnClickListener(new CityRequestListener(sofiaButton.getText().toString(),"Bulgaria",SearchActivity.this));
        plovdivButton.setOnClickListener(new CityRequestListener(plovdivButton.getText().toString(),"Bulgaria",SearchActivity.this));
        varnaButton.setOnClickListener(new CityRequestListener(varnaButton.getText().toString(),"Bulgaria",SearchActivity.this));
        burgasButton.setOnClickListener(new CityRequestListener(burgasButton.getText().toString(),"Bulgaria",SearchActivity.this));
        plevenButton.setOnClickListener(new CityRequestListener(plevenButton.getText().toString(),"Bulgaria",SearchActivity.this));
        ruseButton.setOnClickListener(new CityRequestListener(ruseButton.getText().toString(),"Bulgaria",SearchActivity.this));

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
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(cityName,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addresses.get(0);
        country = address.getCountryName();
        Log.e("country" , country);
        intent = new Intent(this, RequestWeatherIntentService.class);
        intent.putExtra("city", cityName);
        intent.putExtra("country", country);
        startService(intent);
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(this, "You did something wrong", Toast.LENGTH_SHORT).show();
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

    class SearchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context,WeatherActivity.class);
            intent1.putExtra("refresh", "refresh");
            startActivity(intent1);
            finish();
        }
    }
}
