package com.example.kali.weathy.model;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.kali.weathy.LoadingActivity;
import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by iliqn on 16.10.2016 г..
 */

public class GPSTask extends AsyncTask<Void,Void,Void> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private String latitude;
    private String longtitude;
    private String cityName;
    private String country;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Intent intent;
    private Activity activity;

    public GPSTask(Activity activity){
        this.activity = activity;
    }
    @Override
    protected Void doInBackground(Void... params) {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) activity)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        while (mLastLocation == null) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity
                    , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return null;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }
        if (mLastLocation != null) {
            latitude = Double.toString(mLastLocation.getLatitude());
            longtitude = Double.toString(mLastLocation.getLongitude());
            Log.e("lat" , latitude);
            Log.e("long" , longtitude);
            double lat = Double.valueOf(mLastLocation.getLatitude());
            double lng = Double.valueOf(mLastLocation.getLongitude());
            Geocoder gcd = new Geocoder(activity, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(lat, lng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("address" , addresses.toString());
            if (addresses.size() > 0) {/*
                        cityName = addresses.get(0).getLocality();
                        country = addresses.get(0).getCountryName();*/
                cityName = LoadingActivity.bulgariansToEngTranlit(addresses.get(0).getLocality());
                country = LoadingActivity.bulgariansToEngTranlit(addresses.get(0).getCountryName());

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        intent = new Intent(activity, RequestWeatherIntentService.class);
        intent.putExtra("city", cityName);
        intent.putExtra("country", country);
        activity.startService(intent);
        super.onPostExecute(aVoid);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
}
