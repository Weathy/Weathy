package com.example.kali.weathy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kali.weathy.database.AlarmReceiver;
import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.DeviceBootReceiver;
import com.example.kali.weathy.database.RequestWeatherIntentService;
import com.example.kali.weathy.model.GPSTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoadingActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private ProgressBar loadingProgressBar;
    private Intent intent;
    private FirstQueryReceiver firstQueryReceiver;
    private ErrorReceiver secondReceiver;
    private GPSReceive gpsReveicer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        secondReceiver = new ErrorReceiver();
        registerReceiver(secondReceiver, new IntentFilter("Error"));
        firstQueryReceiver = new FirstQueryReceiver();
        registerReceiver(firstQueryReceiver, new IntentFilter("FirstQueryComplete"));
        gpsReveicer = new GPSReceive();
        registerReceiver(gpsReveicer, new IntentFilter("GPSChanged"));
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
                            LoadingActivity.this.finish();

                        }
                    });
                    builder.create().show();
                } else {
                    new GPSTask(LoadingActivity.this).execute();
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
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}


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
            new GPSTask(LoadingActivity.this).execute();
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

    public static String bulgariansToEngTranlit (String text){
        char[] abcCyr =   {' ','а','б','в','г','д','е','ё', 'ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я','А','Б','В','Г','Д','Е','Ё', 'Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Б','Э','Ю','Я','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        String[] abcLat = {" ","a","b","v","g","d","e","e","s","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sch", "u","i", "","e","ju","a","A","B","V","G","D","E","E","Zh","Z","I","Y","K","L","M","N","O","P","R","S","T","U","F","H","Ts","Ch","Sh","Sch", "","I", "","E","Ju","Ja","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            for(int x = 0; x < abcCyr.length; x++ )
                if (text.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
        }
        return builder.toString();
    }
}

