package com.example.kali.weathy.database;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(DBManager.getInstance(context).getLastWeather().getCityName() != null) {
            Intent intent1 = new Intent(context, RequestWeatherIntentService.class);
            intent.putExtra("city", DBManager.getInstance(context).getLastWeather().getCityName().split(",")[0]);
            intent.putExtra("country", DBManager.getInstance(context).getLastWeather().getCityName().split(" ")[1]);
            context.startService(intent1);
        }

    }
}
