package com.example.kali.weathy.database;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import com.example.kali.weathy.Widget;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

public class RequestWeatherIntentService extends IntentService {

    private static final double KELVIN_CONSTANT = 272.15;
    public static final int OPENWEATHER_ERROR_CODE = 500;
    private StringBuilder weatherJSON = new StringBuilder();
    private int currentTemp;
    private String feelsLike;
    private String cityName;
    private String description;
    private int temp_min;
    private int temp_max;
    private String sunrise;
    private String sunset;
    private double windSpeed;
    private int humidity;
    private int pressure;
    private String visibility;
    private String lastUpdate;
    private String date;
    private int maxTemp;
    private int minTemp;
    private String condition;
    private Double tenDayWindSpeed;
    private int tenDayHumidity;
    private String weekDay;
    private int yearDay;
    private int year;
    private String dayLength;
    private int hourlyCurrentTemp;
    private int hourlyFeelsLike;
    private double hourlyWindSpeed;
    private int hourlyHumidity;
    private String hourlyCondition;
    private int hourlyAirPressure;
    private String hourlyTime;
    private String hourlyIconURL;
    private String hourlyDate;
    private String latitude;
    private String longtitude;
    private StringBuilder lastUpdateBuilder;

    public RequestWeatherIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        DBManager.getInstance(getApplicationContext()).getWritableDatabase().execSQL("delete from weather");
        String city = intent.getStringExtra("city");
        String country = intent.getStringExtra("country");
        String fromAlarm = intent.getStringExtra("alarm");
        try {
            URL weatherInfo = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=9d01db38e0b771b0eb2fffa9e3640dd9");
            HttpURLConnection weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            if(weatherConnection.getResponseCode()==OPENWEATHER_ERROR_CODE){
                Intent intent1 = new Intent("Error");
                sendBroadcast(intent1);
                return;
            }
            InputStream weatherStream = weatherConnection.getInputStream();
            Scanner weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());
            }

            JSONObject weather = new JSONObject(weatherJSON.toString());
            description = weather.getJSONArray("weather").getJSONObject(0).getString("main");
            temp_min = (int) (weather.getJSONObject("main").getInt("temp_min") - KELVIN_CONSTANT);
            temp_max = (int) (weather.getJSONObject("main").getInt("temp_max") - KELVIN_CONSTANT);
            windSpeed = weather.getJSONObject("wind").getDouble("speed");
            humidity = weather.getJSONObject("main").getInt("humidity");
            pressure = weather.getJSONObject("main").getInt("pressure");
            currentTemp = (int) (weather.getJSONObject("main").getInt("temp") - KELVIN_CONSTANT);

            weatherJSON.delete(0, weatherJSON.length());
            weatherInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/conditions/q/" + country + "/" + city + ".json");
            weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherStream = weatherConnection.getInputStream();
            weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());
            }
            weather = new JSONObject(weatherJSON.toString());
            if(weather.getJSONObject("response").has("error")){
                Intent intent1 = new Intent("Error");
                sendBroadcast(intent1);
                return;
            }
            if( weather.getJSONObject("response").has("results")){
               while(weather.getJSONObject("response").has("results")){
                   city = weather.getJSONObject("response").getJSONArray("results").getJSONObject(0).getString("city");
                   weatherJSON.delete(0, weatherJSON.length());
                   weatherInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/conditions/q/" + country + "/" + city + ".json");
                   weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
                   weatherConnection.setRequestMethod("GET");
                   weatherStream = weatherConnection.getInputStream();
                   weatherScanner = new Scanner(weatherStream);
                   while (weatherScanner.hasNextLine()) {
                       weatherJSON.append(weatherScanner.nextLine());
                   }
                   weather = new JSONObject(weatherJSON.toString());
               }
            }
            feelsLike = weather.getJSONObject("current_observation").getString("feelslike_c");
            Calendar c = Calendar.getInstance();
            lastUpdateBuilder = new StringBuilder();
            lastUpdateBuilder.append(c.get(Calendar.DAY_OF_MONTH)+".");
            lastUpdateBuilder.append(Integer.toString((c.get(Calendar.MONTH)+1)));
            lastUpdateBuilder.append("."+  c.get(Calendar.YEAR)+",");
            lastUpdateBuilder.append(" " +  c.get(Calendar.HOUR_OF_DAY)+":");
            lastUpdateBuilder.append(c.get(Calendar.MINUTE)+":");
            lastUpdateBuilder.append(c.get(Calendar.SECOND));
            lastUpdate = lastUpdateBuilder.toString();
            cityName = weather.getJSONObject("current_observation").getJSONObject("display_location").getString("full");
            visibility =  weather.getJSONObject("current_observation").getString("visibility_km");
            longtitude = weather.getJSONObject("current_observation").getJSONObject("display_location").getString("longitude");
            latitude = weather.getJSONObject("current_observation").getJSONObject("display_location").getString("latitude");


            weatherJSON.delete(0, weatherJSON.length());
            weatherInfo = new URL("http://api.sunrise-sunset.org/json?lat="+latitude+"&lng="+longtitude+"");
            weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherStream = weatherConnection.getInputStream();
            weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());

            }

            weather = new JSONObject(weatherJSON.toString());
            if(weather.getString("status").equals("INVALID_REQUEST") || weather.getString("status").equals("INVALID_DATE") || weather.getString("status").equals("UNKNOWN_ERROR") ){
                Intent intent1 = new Intent("Error");
                sendBroadcast(intent1);
                return;
            }

            sunset = weather.getJSONObject("results").getString("sunset");
            sunrise = weather.getJSONObject("results").getString("sunrise");

            dayLength = weather.getJSONObject("results").getString("day_length");
            weatherJSON.delete(0, weatherJSON.length());
            DBManager.getInstance(getApplicationContext()).addWeather(cityName, currentTemp, description, temp_min, temp_max, sunrise, sunset, windSpeed + "", humidity, pressure, feelsLike, visibility, lastUpdate,dayLength);

            Intent queryCompleteIntent = new Intent("FirstQueryComplete");
            sendBroadcast(queryCompleteIntent);

            //Ten day data request
            DBManager.getInstance(getApplicationContext()).getWritableDatabase().execSQL("delete from ten_day_forecast");
            weatherJSON.delete(0, weatherJSON.length());
            weatherInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/forecast10day/q/"+city+"," + country + ".json");
            weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherStream = weatherConnection.getInputStream();
            weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());
            }
            weather = new JSONObject(weatherJSON.toString());
            if(weather.getJSONObject("response").has("error")){
                Intent intent1 = new Intent("Error");
                sendBroadcast(intent1);
                return;
            }
            JSONObject forecastJSON = weather.getJSONObject("forecast").getJSONObject("simpleforecast");
            JSONArray forecastArray = forecastJSON.getJSONArray("forecastday");
            JSONObject currentDay;
            for (int i = 0; i < forecastArray.length(); i++) {
                currentDay = new JSONObject(forecastArray.get(i).toString());

                maxTemp = currentDay.getJSONObject("high").getInt("celsius");
                minTemp = currentDay.getJSONObject("low").getInt("celsius");
                condition = currentDay.getString("conditions");
                tenDayWindSpeed = currentDay.getJSONObject("avewind").getDouble("mph");
                tenDayHumidity = currentDay.getInt("avehumidity");
                weekDay = currentDay.getJSONObject("date").getString("weekday");
                yearDay = currentDay.getJSONObject("date").getInt("yday");
                date = currentDay.getJSONObject("date").getString("day") + "." + currentDay.getJSONObject("date").getString("monthname");
                year = currentDay.getJSONObject("date").getInt("year");

                DBManager.getInstance(getApplicationContext()).addTenDayWeather(date, maxTemp, minTemp, condition, tenDayWindSpeed, tenDayHumidity, weekDay, yearDay, year);
            }


            //Twenty four hour data request

            DBManager.getInstance(getApplicationContext()).getWritableDatabase().execSQL("delete from twenty_hour_forecast");
            weatherJSON.delete(0, weatherJSON.length());
            weatherInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/hourly/q/"+city+"," + country + ".json");
            weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherStream = weatherConnection.getInputStream();
            weatherScanner = new Scanner(weatherStream);
            while (weatherScanner.hasNextLine()) {
                weatherJSON.append(weatherScanner.nextLine());
            }
            weather = new JSONObject(weatherJSON.toString());
            if(weather.getJSONObject("response").has("error")){
                Intent intent1 = new Intent("Error");
                sendBroadcast(intent1);
                return;
            }
            JSONArray twentyFourArray = weather.getJSONArray("hourly_forecast");
            for (int i = 0; i < 24; i++) {
                JSONObject obj1 = new JSONObject(twentyFourArray.getJSONObject(i).toString());
                hourlyCurrentTemp = obj1.getJSONObject("temp").getInt("metric");
                hourlyFeelsLike = obj1.getJSONObject("feelslike").getInt("metric");
                hourlyWindSpeed = obj1.getJSONObject("wspd").getDouble("metric");
                hourlyHumidity = obj1.getInt("humidity");
                hourlyCondition = obj1.getString("condition");
                hourlyAirPressure = obj1.getJSONObject("mslp").getInt("metric");
                hourlyTime = obj1.getJSONObject("FCTTIME").getString("hour") + ":" + obj1.getJSONObject("FCTTIME").getString("min");
                hourlyDate = obj1.getJSONObject("FCTTIME").getString("weekday_name") + ", " + obj1.getJSONObject("FCTTIME").getString("mday") + "." + obj1.getJSONObject("FCTTIME").getString("month_name");
                DBManager.getInstance(getApplicationContext()).addTwentyHourWeather(hourlyCurrentTemp, hourlyFeelsLike, hourlyWindSpeed + "", hourlyHumidity, hourlyCondition, hourlyAirPressure, hourlyTime, hourlyDate);
            }

            Intent intent1 = null;

            if(fromAlarm != null){
                intent1 = new Intent(this, Widget.class);
                intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), Widget.class));
                intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(intent1);
            }
            else{
                intent1 = new Intent("SerciveComplete");
                sendBroadcast(intent1);
                DBManager.getInstance(getApplicationContext()).addLastSearch();
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
