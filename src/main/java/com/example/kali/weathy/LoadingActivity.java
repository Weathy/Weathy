package com.example.kali.weathy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.kali.weathy.database.RequestWeatherIntentService;

public class LoadingActivity extends AppCompatActivity{
    private  MyInnerReceiver receiver;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);

        receiver = new MyInnerReceiver();
        registerReceiver(receiver,new IntentFilter("SerciveComplete"));
        Intent intent = new Intent(this, RequestWeatherIntentService.class);
        intent.putExtra("city", "Sofia");
        startService(intent);
    }

    class MyInnerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context,WeatherActivity.class);
            startActivity(intent1);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
}
