package edu.eci.arsw.weather.services.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.eci.arsw.weather.services.WeatherConnectionService;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherConnectionServiceImpl implements WeatherConnectionService {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://api.openweathermap.org/data/2.5/forecast/daily"+
            "?appid=acce0cf6c3df38fa61ad9b193ea4bbba";


    @Override
    public String getCityStats(String city) throws IOException {
        try {
            HttpResponse<String> response = Unirest.get("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=acce0cf6c3df38fa61ad9b193ea4bbba")
                    .asString();
            return response.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return FAILED;
        }
    }
}
