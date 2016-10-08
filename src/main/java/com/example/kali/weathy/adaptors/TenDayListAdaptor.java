package com.example.kali.weathy.adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kali.weathy.R;
import com.example.kali.weathy.model.Weather;

import java.util.List;


public class TenDayListAdaptor extends ArrayAdapter{
    private Context context;
    private List<Weather.TenDayWeather> items;

    public TenDayListAdaptor(Context context, List<Weather.TenDayWeather> items) {
        super(context, R.layout.one_day_view, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.one_day_view, null);
        }

        Weather.TenDayWeather weather = items.get(position);

        TextView day = (TextView) view.findViewById(R.id.day_tv);
        day.setText(weather.getWeekDay() + ", " + weather.getDate());
        TextView minT = (TextView) view.findViewById(R.id.min_temp_tv);
        minT.setText(weather.getMinTemp()+"℃");
        TextView maxT = (TextView) view.findViewById(R.id.max_temp_tv);
        maxT.setText(weather.getMaxTemp()+"℃");

        return view;
    }

}