package com.example.kali.weathy.model;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.kali.weathy.LoadingActivity;
import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class GPSTask extends AsyncTask<Void, Void, Void> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleApiClient mGoogleApiClient;
    private Activity activity;
    private LocationRequest locationRequest;
    private String cityName;
    private String country;
    private Intent intent;
    private LocationManager locationManager;
    private Location mLastLocation;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;

    public GPSTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }

        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                final com.google.android.gms.common.api.Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.e("last", state.toString());
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
                        isGPSEnabled = locationManager.isProviderEnabled(GPS_PROVIDER);
                        isNetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER);
                        if (isGPSEnabled) {
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                mLastLocation = locationManager
                                        .getLastKnownLocation(GPS_PROVIDER);
                                if (mLastLocation != null) {
                                    getLocation();
                                    return;
                                }
                            }
                        }
                        if (isNetworkEnabled) {
                            if (mLastLocation == null) {
                                Log.d("Network", "Network");
                                if (locationManager != null) {
                                    mLastLocation = locationManager
                                            .getLastKnownLocation(NETWORK_PROVIDER);
                                    //updateGPSCoordinates();
                                    Log.e("lastLocFromNetwork", "lastLocFromNetwork");
                                    if(mLastLocation!=null){
                                        getLocation();
                                    }
                                }
                            } else {
                                Log.e("lastLocFromGPS", "lastLocFromGPS");
                                getLocation();
                            }
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e("RESOLUTION_REQUIRED", "RESOLUTION_REQUIRED");
                        try {
                            status.startResolutionForResult(
                                    activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("UNAVAILABLE", "SETTINGS_CHANGE_UNAVAILABLE");
                        break;
                }
            }
        });
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, locationRequest, (LocationListener) activity);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    public void getLocation() {
        double lat = Double.valueOf(mLastLocation.getLatitude());
        double lng = Double.valueOf(mLastLocation.getLongitude());
        Geocoder gcd = new Geocoder(activity, Locale.getDefault());
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
        if(!(cityName.equals(DBManager.getInstance(activity).getLastWeather().getCityName()))) {
            intent = new Intent(activity, RequestWeatherIntentService.class);
            intent.putExtra("city", cityName);
            intent.putExtra("country", country);
            activity.startService(intent);
        }else{
            intent = new Intent(activity, RequestWeatherIntentService.class);
            intent.putExtra("city", DBManager.getInstance(activity).getLastWeather().getCityName().split(",")[0]);
            intent.putExtra("country", DBManager.getInstance(activity).getLastWeather().getCityName().split(" ")[1]);
            activity.startService(intent);
        }
    }
}
