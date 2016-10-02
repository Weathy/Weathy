package com.example.kali.weathy.model;

/**
 * Created by Kali on 28.9.2016 г..
 */

public class Weather {
    private  String cityName;
    private int currentTemp;
    private String description;
    private String icon;
    private int temp_min;
    private int temp_max;
    private String latitude;
    private String longitude;
    private String sunrise;
    private String sunset;
    private double windSpeed;
    private int humidity;
    private int pressure;
    private int feelsLike;
    private double visibility;
    private String lastUpdate;

    public Weather(String cityName,int currentTemp,int feelsLike ,int humidity, double windSpeed, int pressure, String sunrise, String sunset, String description, String icon, int temp_min, int temp_max, String latitude, String longitude,double visibility,String lastUpdate) {
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
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
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

    public int getCurrentTemp() {
        return currentTemp;
    }

    public int getFeelsLike() {
        return feelsLike;
    }

    public double getVisibility() {
        return visibility;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public static class TwentyFourWeather{

        private int currentTemp;
        private String iconURL;
        private int feelsLike;
        private double windSpeed;
        private int humidity;
        private String condition;
        private int airPressure;
        private String time;
        private String date;

        public TwentyFourWeather(int currentTemp, String iconURL, int feelsLike, double windSpeed, int humidity, String condition, int airPressure, String time, String date) {
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

        public double getWindSpeed() {
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
}
