package com.example.kali.weathy.Database;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by iliqn on 3.10.2016 г..
 */
    public class RequestTask extends AsyncTask<Void, Void, Void> {

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

        public  RequestTask(Activity context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL weatherInfo = new URL("http://api.openweathermap.org/data/2.5/weather?q=Sofia&appid=9d01db38e0b771b0eb2fffa9e3640dd9");
                HttpURLConnection weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
                weatherConnection.setRequestMethod("GET");
                InputStream weatherStream = weatherConnection.getInputStream();
                Scanner weatherScanner = new Scanner(weatherStream);
                while (weatherScanner.hasNextLine()) {
                    weatherJSON.append(weatherScanner.nextLine());
                }
                Log.e("error", weatherJSON.toString());

                JSONObject weather = new JSONObject(weatherJSON.toString());
                description = weather.getJSONArray("weather").getJSONObject(0).getString("main");
                icon = weather.getJSONArray("weather").getJSONObject(0).getString("icon");
                temp_min = (int) (weather.getJSONObject("main").getInt("temp_min") - KELVIN_CONSTANT);
                temp_max = (int) (weather.getJSONObject("main").getInt("temp_max") - KELVIN_CONSTANT);
                windSpeed = weather.getJSONObject("wind").getDouble("speed");
                humidity = weather.getJSONObject("main").getInt("humidity");
                pressure = weather.getJSONObject("main").getInt("pressure");
                currentTemp = (int) (weather.getJSONObject("main").getInt("temp") - KELVIN_CONSTANT);
                weatherJSON.delete(0, weatherJSON.length());
                weatherInfo = new URL("http://api.wunderground.com/api/cca5e666b6459f6e/conditions/q/Bulgaria/Sofia.json");
                weatherConnection = (HttpURLConnection) weatherInfo.openConnection();
                weatherConnection.setRequestMethod("GET");
                weatherStream = weatherConnection.getInputStream();
                weatherScanner = new Scanner(weatherStream);
                while (weatherScanner.hasNextLine()) {
                    weatherJSON.append(weatherScanner.nextLine());
                }
                weather = new JSONObject(weatherJSON.toString());
                feelsLike = weather.getJSONObject("current_observation").getInt("feelslike_c");
                String[] up = weather.getJSONObject("current_observation").getString("local_time_rfc822").split("[+]");
                lastUpdate = up[0];
                cityName = weather.getJSONObject("current_observation").getJSONObject("display_location").getString("full");
                weatherJSON.delete(0, weatherJSON.length());
                weatherInfo = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22Sofia%2C%20bg%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
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
                DBManager.getInstance(context).addWeather(cityName,currentTemp,description,temp_min,temp_max,sunrise,sunset,windSpeed+"",humidity,pressure,feelsLike,visibility+"",lastUpdate);

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
