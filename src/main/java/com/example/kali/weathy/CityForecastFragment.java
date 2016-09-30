package com.example.kali.weathy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CityForecastFragment extends Fragment {

    private Button searchButton;
    private CityForecastComunicator activity;

    interface CityForecastComunicator{

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (CityForecastComunicator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_forecast, container, false);
    }

    public static CityForecastFragment newInstance(String str) {

        Bundle args = new Bundle();
        args.putString("str", str);
        
        CityForecastFragment fragment = new CityForecastFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
