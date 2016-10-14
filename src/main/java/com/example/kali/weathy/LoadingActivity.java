package com.example.kali.weathy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class LoadingActivity extends AppCompatActivity{

    private  MyInnerReceiver receiver;
    private ProgressBar loadingProgressBar;
    private Intent intent;
    private ErrorReceiver secondReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        receiver = new MyInnerReceiver();
        registerReceiver(receiver,new IntentFilter("SerciveComplete"));
        secondReceiver = new ErrorReceiver();

        registerReceiver(secondReceiver,new IntentFilter("Error"));
        if(isNetworkAvailable()){
            if(DBManager.getInstance(this).getLastWeather().getCityName()==null){
                intent = new Intent(this, RequestWeatherIntentService.class);
                intent.putExtra("city", "Sofia");
                intent.putExtra("country", "Bulgaria");
                Log.e("internet" , "net" );
                startService(intent);
            }
            else{
                intent = new Intent(this, RequestWeatherIntentService.class);
                intent.putExtra("city", "Sofia");
                Log.e("no internet" , "no net" );
                intent.putExtra("country", "Bulgaria");
                startService(intent);
            }
        }
        else {
            if(DBManager.getInstance(this).getLastWeather().getCityName()==null){
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                loadingProgressBar.setVisibility(View.GONE);
            }
            else{
                intent = new Intent(LoadingActivity.this,WeatherActivity.class);
                startActivity(intent);
                unregisterReceiver(receiver);
                finish();
            }
        }
    }

    class MyInnerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context,WeatherActivity.class);
            startActivity(intent1);
            finish();
        }
    }


    class ErrorReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "No cities match your search query!", Toast.LENGTH_SHORT).show();
            loadingProgressBar.setVisibility(View.GONE);


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
        if(receiver != null){
            try {
                unregisterReceiver(secondReceiver);
                unregisterReceiver(receiver);
            }
            catch (IllegalArgumentException e){

            }
        }
        super.onDestroy();
    }

    private void startAlarm(Context context){
        Intent alrm = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alrm, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), minsToMillis(DeviceBootReceiver.INTERVAL), pendingIntent);
    }

    private long minsToMillis(int x){
        return x*60*1000;
    }

}
