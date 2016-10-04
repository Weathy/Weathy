package com.example.kali.weathy.database;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class TenDayTask extends AsyncTask<Void, Void, Void>{

    private Activity context;
    private StringBuilder tenDayJSON = new StringBuilder();
    private String date;
    private int maxTemp;
    private int minTemp;
    private String condition;
    private String iconURL;
    private Double windSpeed;
    private int humidity;
    private String weekDay;
    private int yearDay;

    public TenDayTask(Activity context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DBManager.getInstance(context).getWritableDatabase().execSQL("delete from ten_day_forecast");
        try {
            URL tenDayInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/forecast10day/q/sofia.json");
            HttpURLConnection connection = (HttpURLConnection) tenDayInfo.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStr = connection.getInputStream();
            Scanner tenDayScanner = new Scanner(inputStr);
            while(tenDayScanner.hasNextLine()){
                tenDayJSON.append(tenDayScanner.nextLine());
            }

            JSONObject jsonObject = new JSONObject(tenDayJSON.toString());
            JSONObject forecastJSON = jsonObject.getJSONObject("forecast").getJSONObject("simpleforecast");
            JSONArray forecastArray = forecastJSON.getJSONArray("forecastday");

            for(int i = 0; i<forecastArray.length(); i++){
                JSONObject currentDay = new JSONObject(forecastArray.get(i).toString());

                maxTemp = currentDay.getJSONObject("high").getInt("celsius");
                minTemp = currentDay.getJSONObject("low").getInt("celsius");
                condition = currentDay.getString("conditions");
                windSpeed = currentDay.getJSONObject("avewind").getDouble("mph");
                humidity = currentDay.getInt("avehumidity");
                weekDay = currentDay.getJSONObject("date").getString("weekday");
                yearDay = currentDay.getJSONObject("date").getInt("yday");
                iconURL = currentDay.getString("icon_url");
                date = currentDay.getJSONObject("date").getString("monthname") + "/" + currentDay.getJSONObject("date").getString("day");
                DBManager.getInstance(context).addTenDayWeather(date, maxTemp, minTemp, condition, windSpeed, humidity, weekDay, yearDay);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
