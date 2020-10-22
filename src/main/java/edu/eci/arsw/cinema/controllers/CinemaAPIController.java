/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.services.CinemaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author cristian
 */
@RestController
@RequestMapping(value = "/cinemas")
public class CinemaAPIController {

    @Autowired
    CinemaServices cinemaServices;

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoCinema(){
        try {
            System.out.println(objectToJson(cinemaServices.getAllCinemas()));
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(objectToJson(cinemaServices.getAllCinemas()),HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 404 Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoCinemaNombre(@PathVariable String name){
        try {
            System.out.println(name);
            String json = objectToJson(cinemaServices.getCinemaByName(name));
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(json,HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 404 Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{name}/{date}")
    public ResponseEntity<?>  manejadorGetRecursoCinemaNombreFecha(
            @PathVariable String name, @PathVariable("date") String fecha){
        String ans = null;
        try {
            System.out.println(fecha);
            System.out.println(fecha);
            ans = objectToJson(cinemaServices.getFunctionsbyCinemaAndExactDay(name, fecha));
            return new ResponseEntity<>(ans,HttpStatus.ACCEPTED);
        } catch (CinemaException ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 404 Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{name}/{date}/{moviename}")
    public ResponseEntity<?>  manejadorGetRecursoCinemaFechaMovie(
            @PathVariable String name, @PathVariable("date") String fecha, @PathVariable String moviename){
        String ans = null;
        try {
            CinemaFunction a = cinemaServices.getFunctionbyCinemaDateAndMovie(name, fecha, moviename);
            ans = objectToJson(a);
            return new ResponseEntity<>(ans,HttpStatus.ACCEPTED);
        } catch (CinemaException ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 404 Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{name}/{date}/{moviename}")
    public ResponseEntity<?> updateFunction(
            @PathVariable String name, @PathVariable("date") String fecha, @PathVariable String moviename, @RequestBody String upate){
        try {
            System.out.println(upate);
            CinemaFunction a = cinemaServices.getFunctionbyCinemaDateAndMovie(name, fecha, moviename);
            System.out.println(a.getDate());
            ObjectReader reader = objectMapper.readerForUpdating(a);
            reader.readValue(upate);
            System.out.println(a.getDate());
            return new ResponseEntity<>("HTTP 200 OK",HttpStatus.OK);
        } catch (CinemaException e){
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>("HTTP 404 Not Found",HttpStatus.NOT_FOUND);
        } catch (IOException e ){
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>("HTTP 400 Bad Request",HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{name}", method = RequestMethod.POST)
    public ResponseEntity<?> controladorNuevoCinema(@RequestBody String body, @PathVariable String name){
        try {
            JsonNode root = objectMapper.readTree(body);
            Cinema nuevo = objectMapper.readValue(body, Cinema.class);
            if(!name.equals(nuevo.getName())){
                return new ResponseEntity<>("HTTP 403 Forbidden",HttpStatus.FORBIDDEN);
            }
            cinemaServices.addNewCinema(nuevo);
            return new ResponseEntity<>("HTTP 201 Created",HttpStatus.CREATED);
        } catch (IOException ex){
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 400 Bad Request",HttpStatus.BAD_REQUEST);
        } catch (CinemaException ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 403 Forbidden",HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value="/{name}/{ndate}", method = RequestMethod.POST)
    public ResponseEntity<?> controladorNuevaFuncion(@RequestBody String body, @PathVariable String name, @PathVariable("ndate") String ndate){
        try {
            JsonNode root = objectMapper.readTree(body);
            System.out.println(body);
            Movie nuevo = objectMapper.readValue(body, Movie.class);
            CinemaFunction cFunction = new CinemaFunction(nuevo, ndate);
            cinemaServices.addFuncion(name,cFunction);
            return new ResponseEntity<>("HTTP 201 Created",HttpStatus.CREATED);
        } catch (IOException ex){
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 400 Bad Request",HttpStatus.BAD_REQUEST);
        } catch (CinemaException ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 403 Forbidden",HttpStatus.FORBIDDEN);
        }
    }


    @RequestMapping(value="/{name}", method = RequestMethod.PUT)
    public ResponseEntity<?>  manejadorUpdateCinema(@RequestBody String body, @PathVariable String name){
        try {
            Cinema point = cinemaServices.getCinemaByName(name);
            cinemaServices.removeCinema(name);
            ObjectReader reader = objectMapper.readerForUpdating(point);
            reader.readValue(body);
            cinemaServices.addNewCinema(point);
            return new ResponseEntity<>("HTTP 202 Accepted",HttpStatus.ACCEPTED);
        } catch (CinemaException ex) {
            return controladorNuevoCinema(body, name);
        } catch (IOException ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 403 Forbidden",HttpStatus.FORBIDDEN);
        }
    }


    @DeleteMapping(value = "/{name}/{date}/{moviename}")
    public ResponseEntity<?> deleteFunction(
            @PathVariable String name, @PathVariable String date, @PathVariable String moviename){
        try {
            cinemaServices.deleteFunctionByNameAndDate(name, date, moviename);
            return new ResponseEntity<>("HTTP 404 Not Found",HttpStatus.OK);
        } catch (CinemaPersistenceException e) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>("HTTP 404 Not Found",HttpStatus.NOT_FOUND);
        }
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
