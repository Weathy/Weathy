package com.example.kali.weathy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.RequestTask;
import com.example.kali.weathy.database.TenDayTask;
import com.example.kali.weathy.database.TwentyFourTask;

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
        if(DBManager.getInstance(this).getLastWeather().getCityName()==null){
            tenDayTask = new TenDayTask(this);
            tasks.put("ten",Boolean.FALSE);
            tenDayTask.execute();
            twentyFourTask = new TwentyFourTask(this);
            tasks.put("twenty",Boolean.FALSE);
            twentyFourTask.execute();
            requestTask = new RequestTask(this);
            tasks.put("task",Boolean.FALSE);
            requestTask.execute("Sofia");
        }
        if(tenDayTask!=null && twentyFourTask!=null && requestTask !=null) {
            Boolean ten = tasks.get("ten");
            Boolean twenty = tasks.get("twenty");
            Boolean task = tasks.get("task");

            while (!(ten) && !(twenty) && !(task)) {

                ten = tasks.get("ten");
                twenty = tasks.get("twenty");
                task = tasks.get("task");
            }
        }
        Intent intent = new Intent(LoadingActivity.this, WeatherActivity.class);
        intent.putExtra("refresh", "refresh");
        startActivity(intent);
        finish();
    }

}
