package com.example.kali.weathy.database;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.kali.weathy.database.DBManager;
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

public class RequestWeatherIntentService extends IntentService {

    private Activity context;
    private static final double KELVIN_CONSTANT = 272.15;
    private StringBuilder weatherJSON = new StringBuilder();
    private int currentTemp;
    private int feelsLike;
    private String cityName;
    private String description;
    private String icon;
    private int temp_min;
    private int temp_max;
    private String sunrise;
    private String sunset;
    private double windSpeed;
    private int humidity;
    private int pressure;
    private double visibility;
    private String lastUpdate;
    private String date;
    private int maxTemp;
    private int minTemp;
    private String condition;
    private String iconURL;
    private Double tenDayWindSpeed;
    private int tenDayHumidity;
    private String weekDay;
    private int yearDay;
    private int year;

    private int hourlyCurrentTemp;
    private int hourlyFeelsLike;
    private double hourlyWindSpeed;
    private int hourlyHumidity;
    private String hourlyCondition;
    private int hourlyAirPressure;
    private String hourlyTime;
    private String hourlyIconURL;
    private String hourlyDate;
    private ArrayList<Weather.TwentyFourWeather> hourlyList  = new ArrayList<>();

    public RequestWeatherIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //DBManager.getInstance(context).getWritableDatabase().execSQL("delete from weather");
        String city = intent.getStringExtra("city");
        try {
            URL weatherInfo = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=9d01db38e0b771b0eb2fffa9e3640dd9");
            HttpURLConnection weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            InputStream weatherStream = weatherConnection.getInputStream();
            Scanner weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());
            }
            Log.e("firstJSON", weatherJSON.toString());

            JSONObject weather = new JSONObject(weatherJSON.toString());
            description = weather.getJSONArray("weather").getJSONObject(0).getString("main");
            icon = weather.getJSONArray("weather").getJSONObject(0).getString("icon");
            temp_min = (int) (weather.getJSONObject("main").getInt("temp_min") - KELVIN_CONSTANT);
            temp_max = (int) (weather.getJSONObject("main").getInt("temp_max") - KELVIN_CONSTANT);
            windSpeed = weather.getJSONObject("wind").getDouble("speed");
            humidity = weather.getJSONObject("main").getInt("humidity");
            pressure = weather.getJSONObject("main").getInt("pressure");
            currentTemp = (int) (weather.getJSONObject("main").getInt("temp") - KELVIN_CONSTANT);
            Log.e("currentTemp",Integer.toString(currentTemp));
            weatherJSON.delete(0, weatherJSON.length());
            weatherInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/conditions/q/Bulgaria/"+city+".json");
            weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherStream = weatherConnection.getInputStream();
            weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());
            }

            Log.e("secondJSON" , weatherJSON.toString());
            weather = new JSONObject(weatherJSON.toString());
            feelsLike = weather.getJSONObject("current_observation").getInt("feelslike_c");
            Log.e("JSONOBJECT" , weather.toString());
            String[] up = weather.getJSONObject("current_observation").getString("local_time_rfc822").split("[+]");
            lastUpdate = up[0];
            cityName = weather.getJSONObject("current_observation").getJSONObject("display_location").getString("full");
            weatherJSON.delete(0, weatherJSON.length());
            weatherInfo = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%2C%20bg%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
            Log.e("thirdJson" , weatherInfo.toString());
            weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherStream = weatherConnection.getInputStream();
            weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());
            }
            weather = new JSONObject(weatherJSON.toString());
            visibility = weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getDouble("visibility");
            sunset = weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunset");
            sunrise = weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunrise");
            Log.e("db" , cityName);
            //DBManager.getInstance(context).addWeather(cityName,currentTemp,description,temp_min,temp_max,sunrise,sunset,windSpeed+"",humidity,pressure,feelsLike,visibility+"",lastUpdate);
            //tenDay
            weatherJSON.delete(0, weatherJSON.length());
            weatherInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/forecast10day/q/"+city+".json");
            weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherStream = weatherConnection.getInputStream();
            weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());
            }
            weather = new JSONObject(weatherJSON.toString());
            JSONObject forecastJSON = weather.getJSONObject("forecast").getJSONObject("simpleforecast");
            JSONArray forecastArray = forecastJSON.getJSONArray("forecastday");
            for(int i = 0; i<forecastArray.length(); i++){
                JSONObject currentDay = new JSONObject(forecastArray.get(i).toString());

                maxTemp = currentDay.getJSONObject("high").getInt("celsius");
                minTemp = currentDay.getJSONObject("low").getInt("celsius");
                condition = currentDay.getString("conditions");
                tenDayWindSpeed = currentDay.getJSONObject("avewind").getDouble("mph");
                tenDayHumidity = currentDay.getInt("avehumidity");
                weekDay = currentDay.getJSONObject("date").getString("weekday");
                yearDay = currentDay.getJSONObject("date").getInt("yday");
                iconURL = currentDay.getString("icon_url");
                date = currentDay.getJSONObject("date").getString("day") + "." + currentDay.getJSONObject("date").getString("monthname");
                year = currentDay.getJSONObject("date").getInt("year");
                //DBManager.getInstance(context).addTenDayWeather(date, maxTemp, minTemp, condition, tenDayWindSpeed, tenDayHumidity, weekDay, yearDay, year);
            }
            weatherJSON.delete(0, weatherJSON.length());
            weatherInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/hourly/q/"+city+".json");
            weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherStream = weatherConnection.getInputStream();
            weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());
            }
            weather = new JSONObject(weatherJSON.toString());
            JSONArray twentyFourArray = weather.getJSONArray("hourly_forecast");
            for(int i = 0; i<24; i++){
                JSONObject obj1 = new JSONObject(twentyFourArray.getJSONObject(i).toString());
                hourlyCurrentTemp = obj1.getJSONObject("temp").getInt("metric");
                hourlyFeelsLike = obj1.getJSONObject("feelslike").getInt("metric");
                hourlyWindSpeed = obj1.getJSONObject("wspd").getDouble("metric");
                hourlyHumidity = obj1.getInt("humidity");
                hourlyCondition= obj1.getString("condition");
                hourlyAirPressure = obj1.getJSONObject("mslp").getInt("metric");
                hourlyTime = obj1.getJSONObject("FCTTIME").getString("hour")+":"+obj1.getJSONObject("FCTTIME").getString("min");
                hourlyIconURL = obj1.getString("icon_url");
                hourlyDate = obj1.getJSONObject("FCTTIME").getString("weekday_name") + ", " + obj1.getJSONObject("FCTTIME").getString("mday") + "." + obj1.getJSONObject("FCTTIME").getString("month_name") + "." + obj1.getJSONObject("FCTTIME").getString("year");
                hourlyList.add(new Weather.TwentyFourWeather(hourlyCurrentTemp, hourlyIconURL, hourlyFeelsLike, Double.toString(hourlyWindSpeed), hourlyHumidity, hourlyCondition, hourlyAirPressure, hourlyTime, hourlyDate));
                //DBManager.getInstance(context).addTwentyHourWeather(hourlyCurrentTemp,hourlyFeelsLike,hourlyWindSpeed+"",hourlyHumidity,hourlyCondition,hourlyAirPressure,hourlyTime,hourlyDate);
                Intent intent1 = new Intent("SerciveComplete");
                sendBroadcast(intent1);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}