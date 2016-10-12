package com.example.kali.weathy.database;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("ALARM", "AlarmReceiver0");

        Intent intent1 = new Intent(context, RequestWeatherIntentService.class);
        intent.putExtra("city", "Sofia");
        intent.putExtra("country", "Bulgaria");
        context.startService(intent1);

        Log.e("ALARM", "AlarmReceiver1");
    }
}
