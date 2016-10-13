package com.example.kali.weathy.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;

public class Weather {
    private  String cityName;
    private int currentTemp;
    private String description;
    private String iconString;
    private int temp_min;
    private int temp_max;
    private String sunrise;
    private String sunset;
    private String windSpeed;
    private int humidity;
    private int pressure;
    private double feelsLike;
    private String visibility;
    private String lastUpdate;
    private String dayLength;

    public Weather(String cityName, int currentTemp, double feelsLike , int humidity, String windSpeed, int pressure, String sunrise, String sunset, String description, String iconString, int temp_min, int temp_max, String visibility, String lastUpdate, String dayLength) {
        this.cityName = cityName;
        this.currentTemp = currentTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.description = description;
        this.iconString = iconString;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.feelsLike = feelsLike;
        this.visibility = visibility;
        this.lastUpdate = lastUpdate;
        this.dayLength = dayLength;
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

    public String getIconString() {
        return iconString;
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

    public double getFeelsLike() {
        return feelsLike;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getDayLength() {
        return dayLength;
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
        public TwentyFourWeather(int currentTemp, String iconURL, int feelsLike, String windSpeed, int humidity, String condition, int airPressure, String time, String date) {
            this.currentTemp = currentTemp;
            this.iconURL = iconURL;
            this.feelsLike = feelsLike;
            this.windSpeed = windSpeed;
            this.humidity = humidity;
            this.condition = condition;
            this.airPressure = airPressure;
            this.time = time;
            this.date = date;

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

        public TenDayWeather(String date, int maxTemp, int minTemp, String condition, String iconURL, Double windSpeed, int humidity, String weekDay, int yearDay, int year) {
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

    }
}
