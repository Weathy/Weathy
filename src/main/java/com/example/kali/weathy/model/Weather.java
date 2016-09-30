package com.example.kali.weathy.model;

/**
 * Created by Kali on 28.9.2016 г..
 */

public class Weather {
    private  String cityName;
    private String description;
    private String icon;
    private int temp_min;
    private int temp_max;
    private double latitude;
    private double longitude;
    private String sunrise;
    private String sunset;
    private double windSpeed;
    private int humidity;
    private int pressure;

    public Weather(String cityName, int humidity, double windSpeed, int pressure, String sunrise, String sunset, String description, String icon, int temp_min, int temp_max, double latitude, double longitude) {
        this.cityName = cityName;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.description = description;
        this.icon = icon;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public int getTemp_min() {
        return temp_min;
    }

    public int getTemp_max() {
        return temp_max;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}
