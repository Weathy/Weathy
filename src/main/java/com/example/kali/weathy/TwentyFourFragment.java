package com.example.kali.weathy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kali.weathy.Adaptors.TwentyFourAdaptor;
import com.example.kali.weathy.model.Weather;

import java.util.ArrayList;


public class TwentyFourFragment extends Fragment {

    private TwentyFourComunicator activity;
    private ArrayList<Weather.TwentyFourWeather> forecast;
    private TwentyFourAdaptor adaptor;


    interface TwentyFourComunicator{
        ArrayList<Weather.TwentyFourWeather> getData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (TwentyFourComunicator) context;
        this.adaptor = null;
        this.forecast = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_twenty_four, container, false);


        forecast = activity.getData();

        forecast.add(new Weather.TwentyFourWeather(22,"http://icons.wxug.com/i/c/k/nt_clear.gif", 22, 22.3, 44, "Clear", 32432, "22:00", "No matter"));
        forecast.add(new Weather.TwentyFourWeather(22,"http://icons.wxug.com/i/c/k/nt_clear.gif", 22, 22.3, 44, "Clear", 32432, "22:00", "No matter"));
        forecast.add(new Weather.TwentyFourWeather(22,"http://icons.wxug.com/i/c/k/nt_clear.gif", 22, 22.3, 44, "Clear", 32432, "22:00", "No fsd"));
        forecast.add(new Weather.TwentyFourWeather(22,"http://icons.wxug.com/i/c/k/nt_clear.gif", 22, 22.3, 44, "Clear", 32432, "22:00", "No fsd"));
        forecast.add(new Weather.TwentyFourWeather(22,"http://icons.wxug.com/i/c/k/nt_clear.gif", 22, 22.3, 44, "Clear", 32432, "22:00", "No fsd"));
        forecast.add(new Weather.TwentyFourWeather(22,"http://icons.wxug.com/i/c/k/nt_clear.gif", 22, 22.3, 44, "Clear", 32432, "22:00", "No fsd"));

        Log.e("hi2", forecast.size()+"");


        adaptor = new TwentyFourAdaptor(getActivity(), forecast);
        RecyclerView rv = (RecyclerView) root.findViewById(R.id.one_hour_recyclerview);
        rv.setAdapter(adaptor);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));


        return root;
    }

    public static TwentyFourFragment newInstance(String str) {

        Bundle args = new Bundle();
        args.putString("str", str);

        TwentyFourFragment fragment = new TwentyFourFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.adaptor.notifyDataSetChanged();
    }


    public void refreshAdaptor(ArrayList<Weather.TwentyFourWeather> list) {
        forecast = new ArrayList<>(list);
        Log.e("hi1", list.size()+"");
        this.adaptor.notifyDataSetChanged();
    }
}
