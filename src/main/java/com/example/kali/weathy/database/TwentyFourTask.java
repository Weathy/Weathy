package com.example.kali.weathy.database;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kali.weathy.LoadingActivity;
import com.example.kali.weathy.WeatherActivity;
import com.example.kali.weathy.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class TwentyFourTask extends AsyncTask<String, Void, Void> {
    private Activity context;
    private StringBuilder twentyFourJSON = new StringBuilder();
    private int currentTemp;
    private int feelsLike;
    private double windSpeed;
    private int humidity;
    private String condition;
    private int airPressure;
    private String time;
    private String iconURL;
    private String date;
    private ArrayList<Weather.TwentyFourWeather> list = new ArrayList<>();

    public TwentyFourTask(Activity context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(String... params) {
        Log.e("database","Twenty");
        DBManager.getInstance(context).getWritableDatabase().execSQL("delete from twenty_hour_forecast");
        try {
            URL twentyFourInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/hourly/q/sofia.json");
            HttpURLConnection connection = (HttpURLConnection) twentyFourInfo.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStr = connection.getInputStream();
            Scanner twentyFourScanner = new Scanner(inputStr);
            while(twentyFourScanner.hasNextLine()){
                twentyFourJSON.append(twentyFourScanner.nextLine());
            }

            JSONObject obj = new JSONObject(twentyFourJSON.toString());
            JSONArray twentyFourArray = obj.getJSONArray("hourly_forecast");
            for(int i = 0; i<24; i++){
                JSONObject obj1 = new JSONObject(twentyFourArray.getJSONObject(i).toString());

                currentTemp = obj1.getJSONObject("temp").getInt("metric");
                feelsLike = obj1.getJSONObject("feelslike").getInt("metric");
                windSpeed = obj1.getJSONObject("wspd").getDouble("metric");
                humidity = obj1.getInt("humidity");
                condition= obj1.getString("condition");
                airPressure = obj1.getJSONObject("mslp").getInt("metric");
                time = obj1.getJSONObject("FCTTIME").getString("hour")+":"+obj1.getJSONObject("FCTTIME").getString("min");
                iconURL = obj1.getString("icon_url");
                date = obj1.getJSONObject("FCTTIME").getString("weekday_name") + ", " + obj1.getJSONObject("FCTTIME").getString("mday") + "." + obj1.getJSONObject("FCTTIME").getString("month_name") + "." + obj1.getJSONObject("FCTTIME").getString("year");
                list.add(new Weather.TwentyFourWeather(currentTemp, iconURL, feelsLike, Double.toString(windSpeed), humidity, condition, airPressure, time, date));
                DBManager.getInstance(context).addTwentyHourWeather(currentTemp,feelsLike,windSpeed+"",humidity,condition,airPressure,time,date);
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
    @Override
    protected void onPostExecute(Void aVoid) {
        if(context instanceof  LoadingActivity){
            Log.e("end","TRUE");
            ((LoadingActivity) context).tasks.put("twenty", Boolean.TRUE);
        }
        Log.e("end","False");
    }

}
