package com.example.kali.weathy;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kali.weathy.database.AlarmReceiver;
import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.DeviceBootReceiver;
import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LoadingActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ProgressBar loadingProgressBar;
    private Intent intent;
    private FirstQueryReceiver firstQueryReceiver;
    private ErrorReceiver secondReceiver;
    private String latitude;
    private String longtitude;
    private String cityName;
    private String country;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GPSReceive gpsReveicer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        secondReceiver = new ErrorReceiver();
        registerReceiver(secondReceiver, new IntentFilter("Error"));
        firstQueryReceiver = new FirstQueryReceiver();
        gpsReveicer = new GPSReceive();
        registerReceiver(gpsReveicer, new IntentFilter("GPSChanged"));
        registerReceiver(firstQueryReceiver, new IntentFilter("FirstQueryComplete"));
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(LoadingActivity.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        Log.e("api", mGoogleApiClient.toString());
        if (isNetworkAvailable()) {
            if (DBManager.getInstance(this).getLastWeather().getCityName() == null) {
                final LocationManager manager = (LocationManager) getSystemService(LoadingActivity.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);
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
                    new GPSTask().execute();
                }
            } else {
                intent = new Intent(this, RequestWeatherIntentService.class);
                intent.putExtra("city", DBManager.getInstance(this).getLastWeather().getCityName().split(",")[0]);
                intent.putExtra("country", DBManager.getInstance(this).getLastWeather().getCityName().split(" ")[1]);
                startService(intent);
            }
        } else {
            if (DBManager.getInstance(this).getLastWeather().getCityName() == null) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                loadingProgressBar.setVisibility(View.GONE);
            } else {
                intent = new Intent(LoadingActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    class ErrorReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "No cities match your search query!", Toast.LENGTH_SHORT).show();
            loadingProgressBar.setVisibility(View.GONE);


        }
    }

    class FirstQueryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context, WeatherActivity.class);
            startActivity(intent1);
            finish();

        }
    }

    class GPSReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("asd", "asd");
            new GPSTask().execute();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onDestroy() {

        try {
            unregisterReceiver(secondReceiver);
            unregisterReceiver(firstQueryReceiver);
            unregisterReceiver(gpsReveicer);
        } catch (IllegalArgumentException e) {

        }
        super.onDestroy();
    }

    private void startAlarm(Context context) {
        Intent alrm = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alrm, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), minsToMillis(DeviceBootReceiver.INTERVAL), pendingIntent);
    }

    private long minsToMillis(int x) {
        return x * 60 * 1000;
    }

    class GPSTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Log.e("getLocation", "fafs");
            while (mLastLocation == null) {
                if (ActivityCompat.checkSelfPermission(LoadingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoadingActivity.this
                        , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return null;
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                }
                if (mLastLocation != null) {
                    Log.e("location", "!=null");
                    latitude = Double.toString(mLastLocation.getLatitude());
                    longtitude = Double.toString(mLastLocation.getLongitude());
                    double lat = Double.valueOf(mLastLocation.getLatitude());
                    double lng = Double.valueOf(mLastLocation.getLongitude());
                    Log.e("latitude", latitude);
                    Log.e("longitude", longtitude);
                    Geocoder gcd = new Geocoder(LoadingActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(lat, lng, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        cityName = addresses.get(0).getLocality();
                        country = addresses.get(0).getCountryName();
                        intent = new Intent(LoadingActivity.this, RequestWeatherIntentService.class);
                        intent.putExtra("city", cityName);
                        intent.putExtra("country", country);
                        startService(intent);

                    }
                }

            return null;
        }
    }
}

