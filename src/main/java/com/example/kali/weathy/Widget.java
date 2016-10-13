package com.example.kali.weathy;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.kali.weathy.database.DBManager;

public class Widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i = 0; i<appWidgetIds.length; i++){
            int currentWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.weathy_widget_info);

            views.setTextViewText(R.id.widnget_current_temp_tv, DBManager.getInstance(context).getLastWeather().getCurrentTemp()+"℃");
            views.setTextViewText(R.id.widget_city_name_tv, DBManager.getInstance(context).getLastWeather().getCityName()+"");
            views.setTextViewText(R.id.widget_condition_tv, DBManager.getInstance(context).getLastWeather().getDescription()+"");
            views.setTextViewText(R.id.widget_min_temp_tv, DBManager.getInstance(context).getTenDayForecast().get(0).getMinTemp()+"℃");
            views.setTextViewText(R.id.widget_max_temp_tv, " | " + DBManager.getInstance(context).getTenDayForecast().get(0).getMaxTemp()+"℃");

            appWidgetManager.updateAppWidget(currentWidgetId,views);

        }
    }
}
