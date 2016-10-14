package com.example.kali.weathy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kali.weathy.model.Weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBManager extends SQLiteOpenHelper {

    private static DBManager ourInstance;
    private static int version = 1;
    private Context context;
    private Weather lastWeather;
    private List<Weather.TwentyFourWeather> twentyHourForecastObjects;
    private List<Weather.TenDayWeather> tenDayForecast;
    private Map<String, String> lastSearch;

    public static DBManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DBManager(context);
        }
        return ourInstance;
    }

    private DBManager(Context context) {
        super(context, "myDB", null, version);
        this.context = context;
        lastWeather = new Weather(null, 0, null, 0, null, 0, null, null, null, null, 0, 0, null, null, null);
        twentyHourForecastObjects = new ArrayList<>();
        tenDayForecast = new ArrayList<>();
        lastSearch = new HashMap<>();
        loadData();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE weather (cityName text, currentTemp INTEGER, condition text,temp_min INTEGER,temp_max INTEGER, sunrise text, sunset text, windSpeed text, humidity INTEGER, pressure INTEGER, feels_like REAL, visibility text,last_update text, day_length text )");
        db.execSQL("CREATE TABLE twenty_hour_forecast (currentTemp INTEGER,feels_like text, windSpeed text, humidity INTEGER, condition text, pressure INTEGER,time text, date text)");
        db.execSQL("CREATE TABLE ten_day_forecast (date text, min_temp INTEGER, max_temp INTEGER, condition text, windspeed REAL, humidity INTEGER, weekday text, yearday INTEGER, year INTEGER)");
        db.execSQL("CREATE TABLE last_search (city text, country text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addWeather(String cityName, int currentTemp, String condition, int temp_min, int temp_max, String sunrise, String sunset, String windSpeed, int humidity, int pressure, String feels_like, String visibility, String last_update, String dayLength) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
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
        values.put("day_length", dayLength);
        database.insert("weather", null, values);
        lastWeather = new Weather(cityName, currentTemp, feels_like, humidity, windSpeed, pressure, sunrise, sunset, condition, null, temp_min, temp_max, visibility, last_update, dayLength);
        database.setTransactionSuccessful();
        database.endTransaction();

    }

    public void addTwentyHourWeather(int currentTemp, int feels_like, String windSpeed, int humidity, String condition, int pressure, String time, String date) {
        if (twentyHourForecastObjects.size() == 24) {
            twentyHourForecastObjects = new ArrayList<>();
        }

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        database.beginTransaction();
        values.put("currentTemp", currentTemp);
        values.put("feels_like", feels_like);
        values.put("windSpeed", windSpeed);
        values.put("humidity", humidity);
        values.put("condition", condition);
        values.put("pressure", pressure);
        values.put("time", time);
        values.put("date", date);
        database.insert("twenty_hour_forecast", null, values);
        twentyHourForecastObjects.add(new Weather.TwentyFourWeather(currentTemp, null, feels_like, windSpeed, humidity, condition, pressure, time, date));
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void addTenDayWeather(String date, int max_temp, int min_temp, String condition, Double windspeed, int humidity, String weekday, int yearday, int year) {
        if (tenDayForecast.size() == 10) {
            tenDayForecast = new ArrayList<>();
        }

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
        values.put("year", year);
        database.insert("ten_day_forecast", null, values);
        tenDayForecast.add(new Weather.TenDayWeather(date, max_temp, min_temp, condition, null, windspeed, humidity, weekday, yearday, year));
        database.setTransactionSuccessful();
        database.endTransaction();

    }

    public void addLastSearch(){
        String city = lastWeather.getCityName().split(",")[0];
        String country = lastWeather.getCityName().split(" ")[1];

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        database.beginTransaction();
        values.put("city", city);
        values.put("country", country);
        database.insert("last_search", null, values);
        lastSearch.put(city, country);
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

    public List<String> getLastSearchCities(){
        return new ArrayList<>(lastSearch.keySet());
    }

    public String[] getRequestData(String city){
        String[] data = new String[2];
        data[0] = city;
        data[1] = lastSearch.get(city);
        return data;
    }

    private void loadData() {
        if (lastWeather.getCityName() == null) {
            Cursor cursor = getWritableDatabase().rawQuery("SELECT cityName, currentTemp, condition,temp_min,temp_max, sunrise, sunset, windSpeed, humidity, pressure, feels_like, visibility,last_update, day_length  FROM  weather", null);
            while (cursor.moveToNext()) {
                String cityName = cursor.getString(cursor.getColumnIndex("cityName"));
                int currentTemp = cursor.getInt(cursor.getColumnIndex("currentTemp"));
                String condition = cursor.getString(cursor.getColumnIndex("condition"));
                int temp_min = cursor.getInt(cursor.getColumnIndex("temp_min"));
                int temp_max = cursor.getInt(cursor.getColumnIndex("temp_max"));
                String sunrise = cursor.getString(cursor.getColumnIndex("sunrise"));
                String sunset = cursor.getString(cursor.getColumnIndex("sunset"));
                String windSpeed = cursor.getString(cursor.getColumnIndex("windSpeed"));
                int humidity = cursor.getInt(cursor.getColumnIndex("humidity"));
                int pressure = cursor.getInt(cursor.getColumnIndex("pressure"));
                String feels_like = cursor.getString(cursor.getColumnIndex("feels_like"));
                String visibility = cursor.getString(cursor.getColumnIndex("visibility"));
                String last_update = cursor.getString(cursor.getColumnIndex("last_update"));
                String day_length = cursor.getString(cursor.getColumnIndex("day_length"));

                lastWeather = new Weather(cityName, currentTemp, feels_like, humidity, windSpeed, pressure, sunrise, sunset, condition, null, temp_min, temp_max, visibility, last_update, day_length);

            }

            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        if (twentyHourForecastObjects.isEmpty()) {
            Cursor cursor = getWritableDatabase().rawQuery("SELECT currentTemp, feels_like, windSpeed, humidity, condition, pressure, time, date FROM  twenty_hour_forecast", null);
            while (cursor.moveToNext()) {
                int currentTemp = cursor.getInt(cursor.getColumnIndex("currentTemp"));
                int feels_like = cursor.getInt(cursor.getColumnIndex("feels_like"));
                String windSpeed = cursor.getString(cursor.getColumnIndex("windSpeed"));
                int humidity = cursor.getInt(cursor.getColumnIndex("humidity"));
                String condition = cursor.getString(cursor.getColumnIndex("condition"));
                int pressure = cursor.getInt(cursor.getColumnIndex("pressure"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                Weather.TwentyFourWeather weather = new Weather.TwentyFourWeather(currentTemp, null, feels_like, windSpeed, humidity, condition, pressure, time, date);
                twentyHourForecastObjects.add(weather);

            }

            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        if (tenDayForecast.isEmpty()) {
            Cursor cursor = getWritableDatabase().rawQuery("SELECT date, min_temp, max_temp, condition, windspeed, humidity, weekday, yearday, year FROM  ten_day_forecast", null);
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                int min_temp = cursor.getInt(cursor.getColumnIndex("min_temp"));
                int max_temp = cursor.getInt(cursor.getColumnIndex("max_temp"));
                String condition = cursor.getString(cursor.getColumnIndex("condition"));
                double windspeed = cursor.getDouble(cursor.getColumnIndex("windspeed"));
                int humidity = cursor.getInt(cursor.getColumnIndex("humidity"));
                String weekday = cursor.getString(cursor.getColumnIndex("weekday"));
                int yearday = cursor.getInt(cursor.getColumnIndex("yearday"));
                int year = cursor.getInt(cursor.getColumnIndex("year"));

                Weather.TenDayWeather weather = new Weather.TenDayWeather(date, max_temp, min_temp, condition, null, windspeed, humidity, weekday, yearday, year);
                tenDayForecast.add(weather);

            }

            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        if(lastSearch.isEmpty()){
            Cursor cursor = getWritableDatabase().rawQuery("SELECT city, country FROM  last_search", null);
            while(cursor.moveToNext()){
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String country = cursor.getString(cursor.getColumnIndex("country"));
                lastSearch.put(city, country);
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

}
