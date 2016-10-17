package com.example.kali.weathy.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by iliqn on 17.10.2016 г..
 */
public class GPSReceiver extends BroadcastReceiver {
    private LocationManager locationManager;

    public GPSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("change", "on");
                Intent gpsIntent = new Intent("GPSChanged");
                context.sendBroadcast(gpsIntent);
            } else {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Log.e("change", "on");
                    Intent gpsIntent = new Intent("GPSChanged");
                    context.sendBroadcast(gpsIntent);
                }else {
                    Log.e("change", "off");
                }
            }
        }
    }

}