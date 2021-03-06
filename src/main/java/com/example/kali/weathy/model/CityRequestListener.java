package com.example.kali.weathy.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.example.kali.weathy.R;
import com.example.kali.weathy.database.RequestWeatherIntentService;

import dmax.dialog.SpotsDialog;

public class CityRequestListener implements View.OnClickListener {
    private String city;
    private String country;
    private Intent intent;
    private Activity activity;
    private SpotsDialog dialog;

    public CityRequestListener(String city,String country,Activity activity){
        this.city = city;
        this.country = country;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        dialog = new SpotsDialog(activity,R.style.Dialog_Style);
        dialog.setCancelable(false);
        dialog.show();
        if(!isNetworkAvailable()){
            dialog.dismiss();
            Toast.makeText(activity, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            return;
        }
        intent = new Intent(activity, RequestWeatherIntentService.class);
        intent.putExtra("city" , city);
        intent.putExtra("country" , country);
        activity.startService(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
