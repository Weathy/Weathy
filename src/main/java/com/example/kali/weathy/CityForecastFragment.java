package com.example.kali.weathy;

import android.content.Context;
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

    private CityForecastComunicator activity;
    private View view;
    private Weather weather;

    interface CityForecastComunicator {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (CityForecastComunicator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_city_forecast, container, false);
        weather = DBManager.getInstance(getActivity()).getLastWeather();
        TextView cityNameTV = (TextView) getActivity().findViewById(R.id.city_name_textview);
        cityNameTV.setText(weather.getCityName());
        TextView visibilityTV = (TextView) view.findViewById(R.id.visibility_textview);
        visibilityTV.setText(weather.getVisibility() + "");
        TextView lastUpdateTV = (TextView) getActivity().findViewById(R.id.renewed_textview);
        lastUpdateTV.setText(weather.getLastUpdate());
        TextView sunsetTV = (TextView) view.findViewById(R.id.time_sunset_textview);
        sunsetTV.setText(weather.getSunset());
        TextView feelsLikeTV = (TextView) view.findViewById(R.id.temperature_status_textview);
        feelsLikeTV.setText(weather.getFeelsLike() + "");
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
        feelsLike.setText(weather.getFeelsLike() + "");

        ImageView icon = (ImageView) view.findViewById(R.id.weather_status_imageview);
        icon.setImageBitmap(weather.getIcon());

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

    public static CityForecastFragment newInstance(String str) {

        Bundle args = new Bundle();
        args.putString("str", str);

        CityForecastFragment fragment = new CityForecastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        weather = DBManager.getInstance(getActivity()).getLastWeather();
        super.onResume();
    }
}
