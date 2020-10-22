/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.weather.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.arsw.weather.services.WeatherStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author cristian
 */
@RestController
@RequestMapping(value = "/weather")
public class WeatherAPIController {

    @Autowired
    private WeatherStatService weatherStatService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getCityInfo(@RequestParam String city) {
        String stats = weatherStatService.getCityStats(city);
        if(stats.equals(WeatherStatService.FAILED)){
            return new ResponseEntity<>("400 BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(stats, HttpStatus.OK);

    }

    private String objectToJson(Object a){
        String json = null;
        try {
            json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(a);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
