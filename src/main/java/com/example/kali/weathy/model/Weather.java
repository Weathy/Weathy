package com.example.kali.weathy.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;

public class Weather {
    private  String cityName;
    private int currentTemp;
    private String description;
    private String icon;
    private int temp_min;
    private int temp_max;
    private String sunrise;
    private String sunset;
    private String windSpeed;
    private int humidity;
    private int pressure;
    private int feelsLike;
    private String visibility;
    private String lastUpdate;

    public Weather(String cityName,int currentTemp,int feelsLike ,int humidity, String windSpeed, int pressure, String sunrise, String sunset, String description, String icon, int temp_min, int temp_max, String visibility,String lastUpdate) {
        this.cityName = cityName;
        this.currentTemp = currentTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.description = description;
        this.icon = icon;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.feelsLike = feelsLike;
        this.visibility = visibility;
        this.lastUpdate = lastUpdate;
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

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public int getFeelsLike() {
        return feelsLike;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public static class TwentyFourWeather implements Serializable {

        private int currentTemp;
        private String iconURL;
        private int feelsLike;
        private String windSpeed;
        private int humidity;
        private String condition;
        private int airPressure;
        private String time;
        private String date;
        private Bitmap icon;

        public TwentyFourWeather(int currentTemp, String iconURL, int feelsLike, String windSpeed, int humidity, String condition, int airPressure, String time, String date, Bitmap icon) {
            this.currentTemp = currentTemp;
            this.iconURL = iconURL;
            this.feelsLike = feelsLike;
            this.windSpeed = windSpeed;
            this.humidity = humidity;
            this.condition = condition;
            this.airPressure = airPressure;
            this.time = time;
            this.date = date;
            this.icon = icon;

            Log.e("BLOB", "this one was created");

        }

        public int getCurrentTemp() {
            return currentTemp;
        }

        public String getIconURL() {
            return iconURL;
        }

        public int getFeelsLike() {
            return feelsLike;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public int getHumidity() {
            return humidity;
        }

        public String getCondition() {
            return condition;
        }

        public int getAirPressure() {
            return airPressure;
        }

        public String getTime() {
            return time;
        }

        public String getDate() {
            return date;
        }

        public Bitmap getIcon() {
            return icon;
        }

    }

    public static class TenDayWeather implements Serializable{

        private String date;
        private int maxTemp;
        private int minTemp;
        private String condition;
        private String iconURL;
        private Double windSpeed;
        private int humidity;
        private String weekDay;
        private int yearDay;
        private int year;
        private Bitmap icon;

        public TenDayWeather(String date, int maxTemp, int minTemp, String condition, String iconURL, Double windSpeed, int humidity, String weekDay, int yearDay, int year, Bitmap icon) {
            this.date = date;
            this.maxTemp = maxTemp;
            this.minTemp = minTemp;
            this.condition = condition;
            this.iconURL = iconURL;
            this.windSpeed = windSpeed;
            this.humidity = humidity;
            this.weekDay = weekDay;
            this.yearDay = yearDay;
            this.year = year;
            this.icon = icon;
        }

        public String getDate() {
            return date;
        }

        public int getMaxTemp() {
            return maxTemp;
        }

        public int getMinTemp() {
            return minTemp;
        }

        public String getCondition() {
            return condition;
        }

        public String getIconURL() {
            return iconURL;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }

        public int getHumidity() {
            return humidity;
        }

        public String getWeekDay() {
            return weekDay;
        }

        public int getYearDay() {
            return yearDay;
        }

        public int getYear() {
            return year;
        }

        public Bitmap getIcon() {
            return icon;
        }
    }
}
