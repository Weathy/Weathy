package com.example.kali.weathy.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kali.weathy.R;
import com.example.kali.weathy.model.Weather;

import java.util.Calendar;
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
        if (items.size() != 0) {
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
    }

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
        TwentyFourListAdaptor.setIcon(icon, weather.getCondition());

        TextView date = (TextView) view.findViewById(R.id.date_textview);

        firstHour = Integer.parseInt(weather.getTime().split(":")[0]);

        if (position == 0) {
            view.findViewById(R.id.card_view_first).setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            date.setText(firstDate);
        } else {
            if (position == secondDatePlace + 1) {
                view.findViewById(R.id.card_view_first).setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                date.setText(secondDate);
            } else {
                view.findViewById(R.id.card_view_first).setVisibility(View.GONE);
                date.setVisibility(View.GONE);
            }
        }
        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public static void setIcon(ImageView view, String condition) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour > 7 && hour < 20) {
            switch (condition) {
                case "Chance of Flurries":
                    view.setImageResource(R.drawable.flurryes);
                    break;
                case "Chance of Rain":
                    view.setImageResource(R.drawable.chance_of_rain);
                    break;
                case "Chance of Sleet":
                    view.setImageResource(R.drawable.not_available);
                    break;
                case "Chance of Snow":
                    view.setImageResource(R.drawable.chance_of_snow);
                    break;
                case "Chance of a Thunderstorm":
                    view.setImageResource(R.drawable.thunderstorm_day);
                    break;
                case "Clear":
                    view.setImageResource(R.drawable.clear_day);
                    break;
                case "Cloudy":
                    view.setImageResource(R.drawable.day_cloudy);
                    break;
                case "Flurries":
                    view.setImageResource(R.drawable.flurryes);
                    break;
                case "Hazy":
                    view.setImageResource(R.drawable.hazy);
                    break;
                case "Mostly Cloudy":
                    view.setImageResource(R.drawable.mostly_cloudy_day);
                    break;
                case "Mostly Sunny":
                    view.setImageResource(R.drawable.partly_sunny_icon);
                    break;
                case "Partly Cloudy":
                    view.setImageResource(R.drawable.mostly_cloudy_day);
                    break;
                case "Partly Sunny":
                    view.setImageResource(R.drawable.partly_sunny);
                    break;
                case "Rain":
                    view.setImageResource(R.drawable.chance_of_rain);
                    break;
                case "Sleet":
                    view.setImageResource(R.drawable.not_available);
                    break;
                case "Snow":
                    view.setImageResource(R.drawable.snow);
                    break;
                case "Sunny":
                    view.setImageResource(R.drawable.clear_day);
                    break;
                case "Thunderstorm":
                    view.setImageResource(R.drawable.thunderstorm_day);
                    break;
                default:
                    view.setImageResource(R.drawable.not_available);
                    break;
            }
        } else {
            switch (condition) {
                case "Chance of Flurries":
                    view.setImageResource(R.drawable.flurryes);
                    break;
                case "Chance of Rain":
                    view.setImageResource(R.drawable.chance_of_rain);
                    break;
                case "Chance of Sleet":
                    view.setImageResource(R.drawable.chance_of_sleet);
                    break;
                case "Chance of Snow":
                    view.setImageResource(R.drawable.snow);
                    break;
                case "Chance of a Thunderstorm":
                    view.setImageResource(R.drawable.thunderstorm_night);
                    break;
                case "Clear":
                    view.setImageResource(R.drawable.clear_night);
                    break;
                case "Cloudy":
                    view.setImageResource(R.drawable.night_cloudy);
                    break;
                case "Flurries":
                    view.setImageResource(R.drawable.flurryes);
                    break;
                case "Hazy":
                    view.setImageResource(R.drawable.hazy);
                    break;
                case "Mostly Cloudy":
                    view.setImageResource(R.drawable.mostly_cloudy_night);
                    break;
                case "Partly Cloudy":
                    view.setImageResource(R.drawable.partly_cloudy_night);
                    break;
                case "Rain":
                    view.setImageResource(R.drawable.chance_of_rain);
                    break;
                case "Sleet":
                    view.setImageResource(R.drawable.chance_of_sleet);
                    break;
                case "Overcast":
                    view.setImageResource(R.drawable.mostly_cloudy_night);
                    break;
                case "Snow":
                    view.setImageResource(R.drawable.snow);
                    break;
                case "Thunderstorm":
                    view.setImageResource(R.drawable.thunderstorm_night);
                    break;
                default:
                    view.setImageResource(R.drawable.not_available);
                    break;
            }
        }
    }
}
