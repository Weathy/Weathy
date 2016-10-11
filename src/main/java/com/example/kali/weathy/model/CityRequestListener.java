package com.example.kali.weathy.model;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.example.kali.weathy.R;
import com.example.kali.weathy.SearchActivity;
import com.example.kali.weathy.database.RequestWeatherIntentService;

/**
 * Created by iliqn on 11.10.2016 г..
 */

public class CityRequestListener implements View.OnClickListener {
    private String city;
    private Intent intent;
    private Activity activity;

    public CityRequestListener(String city,Activity activity){
        this.city = city;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        intent = new Intent(activity, RequestWeatherIntentService.class);
        intent.putExtra("city" , city);
        activity.findViewById(R.id.progress_search).setVisibility(View.VISIBLE);
        activity.startService(intent);
    }
}
