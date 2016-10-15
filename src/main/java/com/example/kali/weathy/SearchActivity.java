package com.example.kali.weathy;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.example.kali.weathy.model.CityRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SearchActivity extends AppCompatActivity implements PlaceSelectionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
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
    private Button gpsButton;
    private Button backButton;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String latitude;
    private String longtitude;
    private ErrorReceiver secondReceiver;
    private FirstQueryReceiver firstQueryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        receiver = new SearchReceiver();
        registerReceiver(receiver, new IntentFilter("SerciveComplete"));
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
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(SearchActivity.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

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
                    final AlertDialog.Builder builder =  new AlertDialog.Builder(SearchActivity.this);
                    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                    final String message = "No GPS Connection! Do you want open GPS setting?";

                    builder.setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface d, int id) {
                            startActivity(new Intent(action));
                            d.dismiss();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface d, int id) {
                            d.cancel();
                        }
                    });
                    builder.create().show();
                } else {
                    getLocation();
                }
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progress_search);
        PlaceAutocompleteFragment fragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_fragment);
        fragment.setOnPlaceSelectedListener(this);
        fragment.setHint("Search for location");
        AutocompleteFilter filter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        fragment.setFilter(filter);
        String condition = getIntent().getStringExtra("condition");

        if (condition != null) {
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
        progressBar.setVisibility(View.VISIBLE);
        cityName = place.getName().toString();
        String [] cityNameConcat = cityName.split(" ");
        if(cityNameConcat.length!=1){
            StringBuilder city = new StringBuilder();
            for (int i = 0 ; i<cityNameConcat.length ; i++){
                 if(i == cityNameConcat.length-1){
                     city.append(cityNameConcat[i]);
                }else {
                     city.append(cityNameConcat[i]).append("%20");
                 }
            }
            cityName = city.toString();
            Log.e("name" , cityName);
        }

        String[] placeAdress = place.getAddress().toString().split(",");
        Log.e("place" , place.getAddress().toString());
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
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
                unregisterReceiver(secondReceiver);
                unregisterReceiver(firstQueryReceiver);
                mGoogleApiClient.disconnect();
            } catch (IllegalArgumentException e) {

            }
        }
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Log.e("connect" , "con");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class SearchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context, WeatherActivity.class);
            intent1.putExtra("refresh", "refresh");
            startActivity(intent1);
            finish();
        }
    }

    class ErrorReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "No cities match your search query!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void getLocation() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e("location", "!=null");
            latitude = Double.toString(mLastLocation.getLatitude());
            longtitude = Double.toString(mLastLocation.getLongitude());
            double lat = Double.valueOf(mLastLocation.getLatitude());
            double lng = Double.valueOf(mLastLocation.getLongitude());
            Log.e("latitude", latitude);
            Log.e("longitude", longtitude);
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(lat, lng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() > 0) {
                this.cityName = addresses.get(0).getLocality();
                this.country = addresses.get(0).getCountryName();
                progressBar.setVisibility(View.VISIBLE);
                intent = new Intent(this, RequestWeatherIntentService.class);
                intent.putExtra("city", cityName);
                intent.putExtra("country", country);
                startService(intent);

            }
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.e("location" , "null");
                return;
            }
            Log.e("con" , "null");

        }
    }


    class FirstQueryReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context,WeatherActivity.class);
            startActivity(intent1);
            finish();

        }
    }



}
