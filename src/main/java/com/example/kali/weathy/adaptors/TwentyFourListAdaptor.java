package com.example.kali.weathy.adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kali.weathy.R;
import com.example.kali.weathy.model.Weather;

import java.util.List;

public class TwentyFourListAdaptor extends ArrayAdapter {

    private Context context;
    private List<Weather.TwentyFourWeather> items;
    private int firstHour;
    private String lastDate;

    public TwentyFourListAdaptor(Context context, List<Weather.TwentyFourWeather> items) {
        super(context, R.layout.one_hour_view, items);
        this.context = context;
        this.items = items;
        this.lastDate = "";
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.one_hour_view, null);
        }

        Weather.TwentyFourWeather weather = items.get(position);

        TextView time = (TextView) view.findViewById(R.id.tf_time_textview);
        time.setText(weather.getTime());
        TextView temp = (TextView) view.findViewById(R.id.tf_temp_textview);
        temp.setText(weather.getCurrentTemp()+"");
        TextView feelslike = (TextView) view.findViewById(R.id.tf_feelslike_textview);
        feelslike.setText(weather.getFeelsLike()+"");

        TextView  date = (TextView) view.findViewById(R.id.date_textview);


        firstHour = Integer.parseInt(weather.getTime().split(":")[0]);

        if(!lastDate.equals(weather.getDate())||(Integer.parseInt(weather.getTime().split(":")[0])-firstHour==position)){
            date.setVisibility(View.VISIBLE);
            date.setText(weather.getDate());
            lastDate = weather.getDate();
        }
        else{
            date.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
