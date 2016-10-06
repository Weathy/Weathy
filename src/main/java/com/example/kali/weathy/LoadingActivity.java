package com.example.kali.weathy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.RequestTask;
import com.example.kali.weathy.database.TenDayTask;
import com.example.kali.weathy.database.TwentyFourTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class LoadingActivity extends AppCompatActivity {

    private TenDayTask tenDayTask;
    private TwentyFourTask twentyFourTask;
    private RequestTask requestTask;

    public static HashMap<String,Boolean> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        tasks = new HashMap<>();

        if(isNetworkAvailable()){
            if(DBManager.getInstance(this).getLastWeather().getCityName()!=null){
                Intent intent = new Intent(LoadingActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                tenDayTask = new TenDayTask(this);
                tasks.put("ten",Boolean.FALSE);
                tenDayTask.execute();
                twentyFourTask = new TwentyFourTask(this);
                tasks.put("twenty",Boolean.FALSE);
                twentyFourTask.execute();
                requestTask = new RequestTask(this);
                tasks.put("task",Boolean.FALSE);
                requestTask.execute("Sofia");

                Boolean ten = tasks.get("ten");
                Boolean twenty = tasks.get("twenty");
                Boolean task = tasks.get("task");

                while (!(ten) && !(twenty) && !(task)) {
                    ten = tasks.get("ten");
                    twenty = tasks.get("twenty");
                    task = tasks.get("task");
                }

                Intent intent = new Intent(LoadingActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();

            }

        }

        else{
            if(DBManager.getInstance(this).getLastWeather().getCityName()!=null){
                Intent intent = new Intent(LoadingActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Please connect to internet, to access the application data for first time", Toast.LENGTH_SHORT).show();
            }
        }

    }

//    private boolean hasActiveInternetConnection(Context context) {
//        if (isNetworkAvailable(context)) {
//            try {
//                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
//                urlc.setRequestProperty("User-Agent", "Test");
//                urlc.setRequestProperty("Connection", "close");
//                urlc.setConnectTimeout(1500);
//                urlc.connect();
//                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
//            } catch (IOException e) {
//                Log.e("error", "Error checking internet connection");
//            }
//        } else {
//            Log.e("error", "No network available!");
//        }
//        return false;
//    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
