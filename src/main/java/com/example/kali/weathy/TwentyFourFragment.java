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
import android.widget.ListView;

import com.example.kali.weathy.adaptors.TwentyFourAdaptor;
import com.example.kali.weathy.adaptors.TwentyFourListAdaptor;
import com.example.kali.weathy.database.DBManager;
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
        forecast = (ArrayList<Weather.TwentyFourWeather>) DBManager.getInstance(getActivity()).getTwentyHourForecastObjects();
//        adaptor = new TwentyFourAdaptor(getActivity(), forecast);
//        RecyclerView rv = (RecyclerView) root.findViewById(R.id.one_hour_recyclerview);
//        rv.setAdapter(adaptor);
//        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        ListView lv = (ListView) root.findViewById(R.id.listview);
        TwentyFourListAdaptor adaptor = new TwentyFourListAdaptor(getActivity(), forecast);
        lv.setAdapter(adaptor);


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
    }


    public void refreshAdaptor(ArrayList<Weather.TwentyFourWeather> list) {
        forecast = new ArrayList<>(list);
        Log.e("hi1", list.size()+"");
    }
}
