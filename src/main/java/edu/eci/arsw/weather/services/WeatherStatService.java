package edu.eci.arsw.weather.services;

public interface WeatherStatService {
    public static String FAILED = "Connection Failed";
    public String getCityStats(String city);
}
