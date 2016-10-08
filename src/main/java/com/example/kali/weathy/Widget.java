package com.example.kali.weathy;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

public class Widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i = 0; i<appWidgetIds.length; i++){
            int currentWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.weathy_widget_info);

            views.setTextViewText(R.id.widnget_timeTV, "Your text");


            appWidgetManager.updateAppWidget(currentWidgetId,views);
        }
    }
}
