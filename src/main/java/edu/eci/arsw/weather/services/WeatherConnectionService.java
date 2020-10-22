package edu.eci.arsw.weather.services;

import java.io.IOException;

public interface WeatherConnectionService {

    public static final String FAILED="Message failed";
    public String getCityStats(String city) throws IOException;
}
