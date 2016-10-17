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

import java.util.Calendar;


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
            sunrise.setText(R.string.sunrise);
            sunsetTV.setText(weather.getSunrise());
        }else {
            sunrise.setText(R.string.sunset);
            sunsetTV.setText(weather.getSunset());
        }

        TextView statusTV = (TextView) view.findViewById(R.id.weather_status_textview);
        statusTV.setText(weather.getDescription());
        TextView degreeTV = (TextView) view.findViewById(R.id.degrees_tv);
        degreeTV.setText(weather.getCurrentTemp() + "");
        TextView airPresure = (TextView) view.findViewById(R.id.air_pressure_textview);
        airPresure.setText(weather.getPressure() + "hPa");
        TextView humidity = (TextView) view.findViewById(R.id.humidity_textview);
        humidity.setText(weather.getHumidity() + "%");
        TextView wind = (TextView) view.findViewById(R.id.wind_meter_in_second_textview);
        wind.setText(weather.getWindSpeed() + "m/s");
        TextView feelsLike = (TextView) view.findViewById(R.id.temperature_status_textview);
        feelsLike.setText(weather.getFeelsLike() + "\u2103");
        TextView dayLength = (TextView) view.findViewById(R.id.day_length_textview);
        dayLength.setText(weather.getDayLength()+"h");
        ImageView icon = (ImageView) view.findViewById(R.id.weather_status_imageview);
        setIcon(icon, weather.getDescription());

        return view;
    }
    @Override
    public void onResume() {
        weather = DBManager.getInstance(getActivity()).getLastWeather();
        super.onResume();
    }

    private void setIcon(ImageView view, String condition){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour > 7 && hour < 20) {
            switch (condition) {
                case "Clear":
                    view.setImageResource(R.drawable.big_clear_day);
                    break;
                case "Clouds":
                    view.setImageResource(R.drawable.big_cloudy);
                    break;
                case "Drizzle":
                    view.setImageResource(R.drawable.big_rain_day);
                    break;
                case "Rain":
                    view.setImageResource(R.drawable.big_rain_day);
                    break;
                case "Thunderstorm":
                    view.setImageResource(R.drawable.big_thunderstorm);
                    break;
                case "Snow":
                    view.setImageResource(R.drawable.big_snow);
                    break;
                case "Atmosphere":
                    view.setImageResource(R.drawable.big_atm);
                    break;
                default:
                    view.setImageResource(R.drawable.big_not_available);
                    break;
            }
        } else {
            switch (condition) {
                case "Clear":
                    view.setImageResource(R.drawable.big_clear_night);
                    break;
                case "Clouds":
                    view.setImageResource(R.drawable.big_cloudy);
                    break;
                case "Drizzle":
                    view.setImageResource(R.drawable.big_rain_night);
                    break;
                case "Rain":
                    view.setImageResource(R.drawable.big_rain_night);
                    break;
                case "Thunderstorm":
                    view.setImageResource(R.drawable.thunderstorm_night);
                    break;
                case "Snow":
                    view.setImageResource(R.drawable.big_snow);
                    break;
                case "Atmosphere":
                    view.setImageResource(R.drawable.big_atm);
                    break;
                default:
                    view.setImageResource(R.drawable.big_not_available);
                    break;
            }
        }
    }
}
