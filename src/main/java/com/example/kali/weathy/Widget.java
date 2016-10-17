package com.example.kali.weathy;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.kali.weathy.database.DBManager;

import java.util.Calendar;

public class Widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for(int i = 0; i<appWidgetIds.length; i++){

            int currentWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.weathy_widget_info);

            views.setTextViewText(R.id.widnget_current_temp_tv, DBManager.getInstance(context).getLastWeather().getCurrentTemp()+"℃");
            views.setTextViewText(R.id.widget_city_name_tv, DBManager.getInstance(context).getLastWeather().getCityName()+"");
            views.setTextViewText(R.id.widget_condition_tv, DBManager.getInstance(context).getLastWeather().getDescription()+"");
            views.setImageViewResource(R.id.widget_icon_imageview, setIcon(DBManager.getInstance(context).getLastWeather().getDescription()));

            if(DBManager.getInstance(context).getTenDayForecast().size() != 0) {
                views.setTextViewText(R.id.widget_min_temp_tv, DBManager.getInstance(context).getTenDayForecast().get(0).getMinTemp() + "℃");
                views.setTextViewText(R.id.widget_max_temp_tv, DBManager.getInstance(context).getTenDayForecast().get(0).getMaxTemp() + "℃");
            }

            Intent launchActivity = new Intent(context, LoadingActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0, launchActivity, 0);
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

            appWidgetManager.updateAppWidget(currentWidgetId,views);
        }
    }

    private int setIcon(String condition){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour > 7 && hour < 20) {
            switch (condition) {
                case "Clear":
                    return R.drawable.big_clear_day;
                case "Clouds":
                    return R.drawable.big_cloudy;
                case "Drizzle":
                    return R.drawable.big_rain_day;
                case "Rain":
                    return R.drawable.big_rain_day;
                case "Thunderstorm":
                     return R.drawable.big_thunderstorm;
                case "Snow":
                    return R.drawable.big_snow;
                case "Atmosphere":
                    return R.drawable.big_atm;
                default:
                    return R.drawable.big_not_available;
            }
        } else {
            switch (condition) {
                case "Clear":
                    return R.drawable.big_clear_night;
                case "Clouds":
                    return R.drawable.big_cloudy;
                case "Drizzle":
                    return R.drawable.big_rain_night;
                case "Rain":
                    return R.drawable.big_rain_night;
                case "Thunderstorm":
                    return R.drawable.thunderstorm_night;
                case "Snow":
                    return R.drawable.big_snow;
                case "Atmosphere":
                    return R.drawable.big_atm;
                default:
                    return R.drawable.big_not_available;
            }
        }
    }

}
