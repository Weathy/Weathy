package com.example.kali.weathy;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.kali.weathy.model.Weather;

public class TwentyFourDialogFragment extends DialogFragment {

    public static TwentyFourDialogFragment newInstance(Weather.TwentyFourWeather weather) {
        TwentyFourDialogFragment fragment = new TwentyFourDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("hour", weather);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Weather.TwentyFourWeather weather = (Weather.TwentyFourWeather) getArguments().getSerializable("hour");

        View root = inflater.inflate(R.layout.fragment_twenty_four_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextView date = (TextView) root.findViewById(R.id.dialog_date_tv);
        date.setText(weather.getTime() + " - " + weather.getDate());
        TextView currentTemp = (TextView) root.findViewById(R.id.dialog_current_temp_tv);
        currentTemp.setText(weather.getCurrentTemp() + "℃");
        TextView condition = (TextView) root.findViewById(R.id.dialog_condition_tv);
        condition.setText(weather.getCondition());
        TextView feelsLike = (TextView) root.findViewById(R.id.dialog_feels_like_tv);
        feelsLike.setText(weather.getFeelsLike() + "℃");
        TextView windSpeed = (TextView) root.findViewById(R.id.dialog_wind_speed_tv);
        windSpeed.setText(weather.getWindSpeed() + "m/s");
        TextView humidity = (TextView) root.findViewById(R.id.dialog_humidity_tv);
        humidity.setText(weather.getHumidity() + "%");
        TextView airPressure = (TextView) root.findViewById(R.id.dialog_air_pressure_tv);
        airPressure.setText(weather.getAirPressure() + "hPa");

        Button close = (Button) root.findViewById(R.id.close_dialog_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwentyFourDialogFragment.this.dismiss();
            }
        });

        return root;
    }



}
