package com.example.kali.weathy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.model.Weather;


public class CityForecastFragment extends Fragment {

    private View view;
    private Weather weather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_city_forecast, container, false);

        weather = DBManager.getInstance(getActivity()).getLastWeather();

        String[] lastUp = DBManager.getInstance(getActivity()).getLastWeather().getLastUpdate().split(" ");
        String [] data = lastUp[1].split(":");

        int hour = Integer.parseInt(data[0]);
        TextView visibilityTV = (TextView) view.findViewById(R.id.visibility_textview);

        if(weather.getVisibility().equals("N/A")){
            visibilityTV.setText(weather.getVisibility());
        }else{
            visibilityTV.setText(weather.getVisibility() + "km");
        }

        TextView sunrise = (TextView) view.findViewById(R.id.sunset_id);
        TextView sunsetTV = (TextView) view.findViewById(R.id.time_sunset_textview);
        if(hour>20 || hour<8){
            sunrise.setText("Sunrise:");
            sunsetTV.setText(weather.getSunrise());
        }else {
            sunrise.setText("Sunset:");
            sunsetTV.setText(weather.getSunset());
        }

        TextView feelsLikeTV = (TextView) view.findViewById(R.id.temperature_status_textview);
        feelsLikeTV.setText(weather.getFeelsLike());
        TextView statusTV = (TextView) view.findViewById(R.id.weather_status_textview);
        statusTV.setText(weather.getDescription());
        TextView degreeTV = (TextView) view.findViewById(R.id.degrees_tv);
        degreeTV.setText(weather.getCurrentTemp() + "");
        TextView airPresure = (TextView) view.findViewById(R.id.air_pressure_textview);
        airPresure.setText(weather.getPressure() + " HPa");
        TextView humidity = (TextView) view.findViewById(R.id.humidity_textview);
        humidity.setText(weather.getHumidity() + "%");
        TextView wind = (TextView) view.findViewById(R.id.wind_meter_in_second_textview);
        wind.setText(weather.getWindSpeed() + "m/s");
        TextView feelsLike = (TextView) view.findViewById(R.id.temperature_status_textview);
        feelsLike.setText(weather.getFeelsLike() + "\u2103");
        TextView dayLength = (TextView) view.findViewById(R.id.day_length_textview);
        dayLength.setText(weather.getDayLength()+"h");
        ImageView icon = (ImageView) view.findViewById(R.id.weather_status_imageview);

         switch (weather.getDescription()) {
            case "Clear":
                getActivity().findViewById(R.id.content).setBackgroundResource(R.drawable.day_clear);
                return view;
            case "Clouds":
                getActivity().findViewById(R.id.content).setBackgroundResource(R.drawable.day_cloudy);
                return view;
            case "Thunderstorm":
                getActivity().findViewById(R.id.content).setBackgroundResource(R.drawable.day_thunderstorm);
                return view;
            case "Rain":
                getActivity().findViewById(R.id.content).setBackgroundResource(R.drawable.rain);
                return view;
        }
        return view;
    }
    @Override
    public void onResume() {
        weather = DBManager.getInstance(getActivity()).getLastWeather();
        super.onResume();
    }
}
