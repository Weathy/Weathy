package com.example.kali.weathy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

public class GPSReceiver extends BroadcastReceiver {
    private LocationManager locationManager;
    public GPSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Log.e("change" , "on");
                Intent gpsIntent = new Intent("GPSChanged");
                context.sendBroadcast(gpsIntent);
            }else{
                Log.e("change" , "off");
            }
        }
    }

}
