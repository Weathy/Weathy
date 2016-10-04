package com.example.kali.weathy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.kali.weathy.model.Weather;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DBManager extends SQLiteOpenHelper{

    private static DBManager ourInstance;
    private static int version = 1;
    private Context context;
    private Weather lastWeather;
    private List<Weather.TwentyFourWeather> twentyHourForecastObjects;
    private List<Weather.TenDayWeather> tenDayForecast;

    public static DBManager getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new DBManager(context);
        }
        return ourInstance;
    }

    private DBManager(Context context) {
        super(context, "myDB", null, version);
        this.context = context;
        twentyHourForecastObjects = new ArrayList<>();
        tenDayForecast = new ArrayList<>();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE weather (cityName text, currentTemp INTEGER, condition text,temp_min INTEGER,temp_max INTEGER, sunrise text, sunset text, windSpeed text, humidity INTEGER, pressure INTEGER, feels_like INTEGER, visibility text,last_update text  )");
        db.execSQL("CREATE TABLE twenty_hour_forecast (currentTemp INTEGER,feels_like INTEGER, windSpeed text, humidity INTEGER, condition text, pressure INTEGER,time text, date text)");
        db.execSQL("CREATE TABLE ten_day_forecast (date text, min_temp INTEGER, max_temp INTEGER, condition text, windspeed REAL, humidity INTEGER, weekday text, yearday INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addWeather(String cityName,int currentTemp,String condition,int temp_min,int temp_max,String sunrise,String sunset,String windSpeed,int humidity,int pressure,int feels_like,String visibility,String last_update) {
            SQLiteDatabase database = getWritableDatabase();
            database.beginTransaction();
            Log.e("db", cityName);
            ContentValues values = new ContentValues();
            values.put("cityName", cityName);
            values.put("currentTemp", currentTemp);
            values.put("condition", condition);
            values.put("temp_min", temp_min);
            values.put("temp_max", temp_max);
            values.put("sunrise", sunrise);
            values.put("sunset", sunset);
            values.put("windSpeed", windSpeed);
            values.put("humidity", humidity);
            values.put("pressure", pressure);
            values.put("feels_like", feels_like);
            values.put("visibility", visibility);
            values.put("last_update", last_update);
            database.insert("weather", null, values);
            lastWeather = new Weather(cityName, currentTemp, feels_like, humidity, windSpeed, pressure, sunrise, sunset, condition, null, temp_min, temp_max, visibility, last_update);
            database.setTransactionSuccessful();
            database.endTransaction();

    }

    public void addTwentyHourWeather(int currentTemp,int feels_like,String windSpeed,int humidity,String condition,int pressure,String time, String date){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        database.beginTransaction();
        values.put("currentTemp" , currentTemp);
        values.put("feels_like" , feels_like);
        values.put("windSpeed" , windSpeed);
        values.put("humidity" , humidity);
        values.put("condition" , condition);
        values.put("pressure" , pressure);
        values.put("time" , time);
        values.put("date" , date);
        database.insert("twenty_hour_forecast", null, values);
        twentyHourForecastObjects.add(new Weather.TwentyFourWeather(currentTemp,null,feels_like,windSpeed,humidity,condition,pressure,time,date));
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void addTenDayWeather(String date, int max_temp, int min_temp, String condition, Double windspeed, int humidity, String weekday, int yearday){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        database.beginTransaction();
        values.put("date", date);
        values.put("max_temp", max_temp);
        values.put("min_temp", min_temp);
        values.put("condition", condition);
        values.put("windspeed", windspeed);
        values.put("humidity", humidity);
        values.put("weekday", weekday);
        values.put("yearday", yearday);
        database.insert("ten_day_forecast", null, values);
        tenDayForecast.add(new Weather.TenDayWeather(date, max_temp, min_temp, condition, null, windspeed, humidity, weekday, yearday));
        database.setTransactionSuccessful();
        database.endTransaction();

    }

    public Weather getLastWeather() {
        return lastWeather;
    }

    public List<Weather.TwentyFourWeather> getTwentyHourForecastObjects() {
        return twentyHourForecastObjects;
    }

    public List<Weather.TenDayWeather> getTenDayForecast() {
        return Collections.unmodifiableList(tenDayForecast);
    }
}
