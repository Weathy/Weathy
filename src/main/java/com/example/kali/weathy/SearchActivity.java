package com.example.kali.weathy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.example.kali.weathy.model.CityRequestListener;
import com.example.kali.weathy.model.GPSTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;


public class SearchActivity extends AppCompatActivity implements PlaceSelectionListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private String cityName;
    private String country;
    private Intent intent;
    private Button sofiaButton;
    private Button plovdivButton;
    private Button varnaButton;
    private Button burgasButton;
    private Button plevenButton;
    private Button ruseButton;
    private Button gpsButton;
    private Button backButton;
    private ErrorReceiver secondReceiver;
    private FirstQueryReceiver firstQueryReceiver;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dialog = new SpotsDialog(this,R.style.Dialog_Style);
        dialog.setCancelable(false);
        secondReceiver = new ErrorReceiver();
        registerReceiver(secondReceiver,new IntentFilter("Error"));
        firstQueryReceiver = new FirstQueryReceiver();
        registerReceiver(firstQueryReceiver,new IntentFilter("FirstQueryComplete"));
        sofiaButton = (Button) findViewById(R.id.sofia_button);
        plovdivButton = (Button) findViewById(R.id.plovdiv_button);
        varnaButton = (Button) findViewById(R.id.varna_button);
        burgasButton = (Button) findViewById(R.id.burgas_button);
        plevenButton = (Button) findViewById(R.id.pleven_button);
        ruseButton = (Button) findViewById(R.id.ruse_button);
        gpsButton = (Button) findViewById(R.id.gps_search_button);

        sofiaButton.setOnClickListener(new CityRequestListener(sofiaButton.getText().toString(), "Bulgaria", SearchActivity.this));
        plovdivButton.setOnClickListener(new CityRequestListener(plovdivButton.getText().toString(), "Bulgaria", SearchActivity.this));
        varnaButton.setOnClickListener(new CityRequestListener(varnaButton.getText().toString(), "Bulgaria", SearchActivity.this));
        burgasButton.setOnClickListener(new CityRequestListener(burgasButton.getText().toString(), "Bulgaria", SearchActivity.this));
        plevenButton.setOnClickListener(new CityRequestListener(plevenButton.getText().toString(), "Bulgaria", SearchActivity.this));
        ruseButton.setOnClickListener(new CityRequestListener(ruseButton.getText().toString(), "Bulgaria", SearchActivity.this));
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    dialog.show();
                    new GPSTask(SearchActivity.this).execute();
                } else {
                    dialog.show();
                    if(!isNetworkAvailable()) {
                        Toast.makeText(SearchActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else {
                        dialog.show();
                        new GPSTask(SearchActivity.this).execute();
                    }
                }
            }
        });

        PlaceAutocompleteFragment fragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_fragment);
        fragment.setOnPlaceSelectedListener(this);
        fragment.setHint("Search for location");
        AutocompleteFilter filter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        fragment.setFilter(filter);
        String condition = getIntent().getStringExtra("condition");

        if (condition != null) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            switch (DBManager.getInstance(this).getLastWeather().getDescription()) {
                case "Clear":
                    if(hour>=20 || hour<=7){
                        findViewById(R.id.activity_search).setBackgroundResource(R.drawable.night_clear);
                        break;
                    }
                    findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_clear);
                    break;
                case "Clouds":
                    if(hour>=20 || hour<=7){
                        findViewById(R.id.activity_search).setBackgroundResource(R.drawable.night_cloudy);
                        break;
                    }
                    findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_cloudy);
                    break;
                case "Thunderstorm":
                    if(hour>=20 || hour<=7){
                        findViewById(R.id.activity_search).setBackgroundResource(R.drawable.night_thunderstorm);
                        break;
                    }
                    findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_thunderstorm);
                    break;
                case "Rain":
                    if(hour>=20 || hour<=7){
                        findViewById(R.id.activity_search).setBackgroundResource(R.drawable.night_rain);
                        break;
                    }
                    findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_rain);
                    break;

            }
        } else {
            findViewById(R.id.activity_search).setBackgroundResource(R.drawable.loading_screen_background);
        }

        backButton = (Button) findViewById(R.id.back_search_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onPlaceSelected(Place place) {
        dialog.show();
        cityName = place.getName().toString();
        String [] cityNameConcat = cityName.split(" ");
        if(cityNameConcat.length!=1){
            cityName = LoadingActivity.bulgariansToEngTranlit(cityName);
            StringBuilder city = new StringBuilder();
            for (int i = 0 ; i<cityNameConcat.length ; i++){
                 if(i == cityNameConcat.length-1){
                     city.append(cityNameConcat[i]);
                }else {
                     city.append(cityNameConcat[i]).append("%20");
                 }
            }
            cityName = city.toString();
        }else{
            cityName = LoadingActivity.bulgariansToEngTranlit(cityName);
        }
        String[] placeAdress = place.getAddress().toString().split(",");
        if(placeAdress.length==1){
            country = placeAdress[0];
        }else {
            if (placeAdress.length != 2) {
                if(placeAdress[placeAdress.length-1].equals(" USA")){
                    country = placeAdress[1];
                }else{
                    country = placeAdress[placeAdress.length - 1];
                }
            } else {
                country = placeAdress[1];
            }
        }
        country = country.trim();
        country = LoadingActivity.bulgariansToEngTranlit(country);
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
        dialog.dismiss();
        try {
            unregisterReceiver(secondReceiver);
            unregisterReceiver(firstQueryReceiver);
        } catch (IllegalArgumentException e) {
         }
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
        double lat = Double.valueOf(location.getLatitude());
        double lng = Double.valueOf(location.getLongitude());
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0) {
            cityName = addresses.get(0).getLocality();
            country = addresses.get(0).getCountryName();
            cityName = LoadingActivity.bulgariansToEngTranlit(addresses.get(0).getLocality());
            country = LoadingActivity.bulgariansToEngTranlit(addresses.get(0).getCountryName());

        }
        if(!(cityName.equals(DBManager.getInstance(this).getLastWeather().getCityName()))) {
            intent = new Intent(this, RequestWeatherIntentService.class);
            intent.putExtra("city", cityName);
            intent.putExtra("country", country);
            startService(intent);
        }else{
            intent = new Intent(this, RequestWeatherIntentService.class);
            intent.putExtra("city", DBManager.getInstance(this).getLastWeather().getCityName().split(",")[0]);
            intent.putExtra("country", DBManager.getInstance(this).getLastWeather().getCityName().split(" ")[1]);
            startService(intent);
        }
    }


    class ErrorReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            dialog.dismiss();
            Toast.makeText(context, "No cities match your search query!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    class FirstQueryReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            setResult(200,null,null);
            finish();
        }
    }
}
