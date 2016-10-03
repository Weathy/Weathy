package com.example.kali.weathy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by iliqn on 3.10.2016 г..
 */
public class DBManager extends SQLiteOpenHelper{

    private static DBManager ourInstance;
    private static int version = 1;
    private Context context;

    public static DBManager getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new DBManager(context);
        }
        return ourInstance;
    }

    private DBManager(Context context) {
        super(context, "myDB", null, version);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE weather (cityName text, currentTemp INTEGER, condition text,temp_min INTEGER,temp_max INTEGER, sunrise text, sunset text, windSpeed text, humidity INTEGER, pressure INTEGER, feels_like INTEGER, visibility text,last_update text  )");
        db.execSQL("CREATE TABLE twenty_hour_forecast (currentTemp INTEGER,feels_like INTEGER, windSpeed text, humidity INTEGER, condition text, pressure INTEGER,time text, date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addWeather(String cityName,int currentTemp,String condition,int temp_min,int temp_max,String sunrise,String sunset,String windSpeed,int humidity,int pressure,int feels_like,String visibility,String last_update) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        Log.e("db" , cityName);
        ContentValues values = new ContentValues();
        values.put("cityName" , cityName);
        values.put("currentTemp" , currentTemp);
        values.put("condition" , condition);
        values.put("temp_min" , temp_min);
        values.put("temp_max" , temp_max);
        values.put("sunrise" , sunrise);
        values.put("sunset" , sunset);
        values.put("windSpeed" , windSpeed);
        values.put("humidity" , humidity);
        values.put("pressure" , pressure);
        values.put("feels_like" , feels_like);
        values.put("visibility" , visibility);
        values.put("last_update" , last_update);
        database.insert("weather", null, values);
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
        database.setTransactionSuccessful();
        database.endTransaction();
    }

}
