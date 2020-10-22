package edu.eci.arsw.weather.services.impl;

import edu.eci.arsw.weather.services.WeatherConnectionService;
import edu.eci.arsw.weather.services.WeatherStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
public class WeatherStatServiceImpl implements WeatherStatService {



    @Autowired
    WeatherConnectionService weatherConnectionService;

    private HashMap<String, String> cache = new HashMap<String, String>();

    public String getCityStats(String city){
        if(!cache.containsKey(city)){
            try {
                String stats = weatherConnectionService.getCityStats(city);
                if(stats.equals(WeatherConnectionService.FAILED)){
                    return FAILED;
                } else {
                    cache.put(city, stats);
                    return stats;
                }
            } catch (IOException e) {
                return WeatherConnectionService.FAILED;
            }
        } else {
            return cache.get(city);
        }
    }
}
