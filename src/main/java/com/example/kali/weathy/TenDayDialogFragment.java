package com.example.kali.weathy;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kali.weathy.model.Weather;

public class TenDayDialogFragment extends DialogFragment {

    public static TenDayDialogFragment newInstance(Weather.TenDayWeather weather) {
        TenDayDialogFragment fragment = new TenDayDialogFragment();
        Bundle args = new Bundle();

        args.putSerializable("day", weather);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Weather.TenDayWeather weather = (Weather.TenDayWeather) getArguments().getSerializable("day");
        View root = inflater.inflate(R.layout.fragment_ten_day_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextView date = (TextView) root.findViewById(R.id.dialog_date_tv);
        date.setText(weather.getWeekDay() + ", " + weather.getDate() + "/" + weather.getYear());
        TextView condition = (TextView) root.findViewById(R.id.dialog_condition_tv);
        condition.setText(weather.getCondition());
        TextView maxTemp = (TextView) root.findViewById(R.id.dialog_max_temp_tv);
        maxTemp.setText(weather.getMaxTemp()+"℃");
        TextView minTemp = (TextView) root.findViewById(R.id.dialog_min_temp_tv);
        minTemp.setText(weather.getMinTemp()+"℃");
        TextView windSpeed = (TextView) root.findViewById(R.id.dialog_wind_speed_tv);
        windSpeed.setText(weather.getWindSpeed()+"m/s");
        TextView humidity = (TextView) root.findViewById(R.id.dialog_humidity_tv);
        humidity.setText(weather.getHumidity()+"%");
        TextView dayOfTheYear = (TextView) root.findViewById(R.id.dialog_day_of_the_year_tv);
        dayOfTheYear.setText(weather.getYearDay()+"");
        ImageView icon = (ImageView) root.findViewById(R.id.dialog_icon_iv);
        icon.setImageBitmap(weather.getIcon());

        Button close = (Button) root.findViewById(R.id.close_dialog_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TenDayDialogFragment.this.dismiss();
            }
        });



        return root;
    }

}
