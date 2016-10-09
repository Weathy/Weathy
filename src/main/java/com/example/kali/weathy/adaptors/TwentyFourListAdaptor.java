package com.example.kali.weathy.adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kali.weathy.R;
import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.model.Weather;

import java.util.List;

public class TwentyFourListAdaptor extends ArrayAdapter {

    private Context context;
    private List<Weather.TwentyFourWeather> items;
    private String firstDate;
    private String secondDate;
    private int secondDatePlace;
    private int firstHour;

    public TwentyFourListAdaptor(Context context, List<Weather.TwentyFourWeather> items) {
        super(context, R.layout.one_hour_view, items);
        this.context = context;
        this.items = items;
        this.firstDate = items.get(0).getDate();
        this.firstHour = Integer.parseInt(items.get(0).getTime().split(":")[0]);

        for (int i = 0; i < items.size(); i++) {
            if (!firstDate.equals(items.get(i).getDate())) {
                secondDate = items.get(i).getDate();
                break;
            }
        }
        secondDatePlace = 23 - firstHour;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.one_hour_view, null);
        }

        Weather.TwentyFourWeather weather = items.get(position);

        TextView time = (TextView) view.findViewById(R.id.tf_time_textview);
        time.setText(weather.getTime());
        TextView temp = (TextView) view.findViewById(R.id.tf_temp_textview);
        temp.setText(weather.getCurrentTemp() + "");
        TextView feelslike = (TextView) view.findViewById(R.id.tf_feelslike_textview);
        feelslike.setText(weather.getFeelsLike() + "");
        ImageView icon = (ImageView) view.findViewById(R.id.tf_icon_imageview);
        icon.setImageBitmap(weather.getIcon());

        TextView date = (TextView) view.findViewById(R.id.date_textview);


        firstHour = Integer.parseInt(weather.getTime().split(":")[0]);

        if (position == 0) {
            date.setVisibility(View.VISIBLE);
            date.setText(firstDate);
        } else {
            if (position == secondDatePlace + 1) {
                date.setVisibility(View.VISIBLE);
                date.setText(secondDate);
            } else {
                date.setVisibility(View.GONE);
            }
        }

        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
