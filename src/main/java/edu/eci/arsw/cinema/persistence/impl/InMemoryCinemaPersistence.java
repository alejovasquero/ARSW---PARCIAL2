/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence.impl;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import edu.eci.arsw.cinema.services.CinemaServices;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author cristian
 */
@Repository
public class InMemoryCinemaPersistence implements CinemaPersitence {

    private final Map<String, Cinema> cinemas = new HashMap<>();

    public InMemoryCinemaPersistence() {
        // load stub data
        String functionDate = "2018-12-18 15:30";
        List<CinemaFunction> functions = new ArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("SuperHeroes Movie", "Action"), functionDate);
        CinemaFunction funct2 = new CinemaFunction(new Movie("The Night", "Horror"), functionDate);
        functions.add(funct1);
        functions.add(funct2);
        Cinema c = new Cinema("cinemaX", functions);
        cinemas.put("cinemaX", c);

        String functionDate1 = "2019-12-18 15:30";
        List<CinemaFunction> functions1 = new ArrayList<>();
        CinemaFunction funct12 = new CinemaFunction(new Movie("IT", "Action"), functionDate1);
        CinemaFunction funct22 = new CinemaFunction(new Movie("FFF", "Horror"), functionDate1);
        functions1.add(funct12);
        functions1.add(funct22);
        Cinema c1 = new Cinema("CineColombia", functions1);
        cinemas.put("CineColombia", c1);

        String functionDate2 = "2038-12-18 15:30";
        List<CinemaFunction> functions2 = new ArrayList<>();
        CinemaFunction funct13 = new CinemaFunction(new Movie("SuperHeroes Movie", "Action"), functionDate2);
        CinemaFunction funct23 = new CinemaFunction(new Movie("The Night", "Horror"), functionDate2);
        functions2.add(funct13);
        functions2.add(funct23);
        Cinema c2 = new Cinema("Premier", functions2);
        cinemas.put("Premier", c2);
    }

    @Override
    public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException {
        try {
            for (CinemaFunction cf : getCinema(cinema).getFunctions()) {
                if (cf.getMovie().getName().equals(movieName) && cf.getDate().equals(date)) {
                    cf.buyTicket(row, col);
                }
            }
        } catch (CinemaPersistenceException e) {
            throw new CinemaException("Error consultando el cinema", e);
        } catch (NullPointerException e) {
            throw new CinemaException("La sesión de esta película no existe", e);
        }
    }

    @Override
    public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date) throws CinemaPersistenceException {
        List<CinemaFunction> ans = null;
        if (cinemas.containsKey(cinema)) {
            ans = cinemas.get(cinema).getFunctions().stream().filter(p -> p.getDate().equals(date))
                    .collect(Collectors.toList());
        } else {
            throw new CinemaPersistenceException("El cinema especificado no existe");
        }
        return ans;
    }

    @Override
    public List<CinemaFunction> getFunctionsbyCinemaAndExactDay(String cinema, String date) throws CinemaPersistenceException {
        List<CinemaFunction> ans = null;
        if (cinemas.containsKey(cinema)) {
            ans = cinemas.get(cinema).getFunctions().stream().filter(p -> p.getDate().contains(date))
                    .collect(Collectors.toList());
        } else {
            throw new CinemaPersistenceException("El cinema especificado no existe");
        }
        return ans;
    }

    @Override
    public CinemaFunction getFunctionbyCinemaDateAndMovie(String cinema, String date, String movie) throws CinemaPersistenceException {
        CinemaFunction ans = null;
        if (cinemas.containsKey(cinema)) {
            List<CinemaFunction> list = cinemas.get(cinema).getFunctions().stream()
                    .filter(p -> p.getDate().equals(date) && p.getMovie().getName().equals(movie))
                    .collect(Collectors.toList());
            ans = list.size() > 0 ? list.get(0) : null;
            if(ans == null){
                throw new CinemaPersistenceException("La película especificada no existe");
            }
        } else {
            throw new CinemaPersistenceException("El cinema especificado no existe");
        }
        return ans;
    }

    @Override
    public void saveCinema(Cinema c) throws CinemaPersistenceException {
        if (cinemas.containsKey(c.getName())) {
            throw new CinemaPersistenceException("The given cinema already exists: " + c.getName());
        } else {
            cinemas.put(c.getName(), c);
        }
    }

    @Override
    public void saveCinemaFunction(String cinema,CinemaFunction cFunction) throws CinemaPersistenceException {
        if (!cinemas.containsKey(cinema)||cinemas.get(cinema).getFunctions().contains(cFunction)) {
            throw new CinemaPersistenceException("The given Function already exists or cinema doesn't exists: " + cFunction.getMovie().getName());
        } else {
            getCinema(cinema).getFunctions().add(cFunction);
        }

    }

    @Override
    public Cinema getCinema(String name) throws CinemaPersistenceException {
        if(cinemas.containsKey(name)){
            return cinemas.get(name);
        }else{
            throw new CinemaPersistenceException("Cinema No Existe");
        }
    }
        


    public Set<Cinema> getCinemas() {
        Set<Cinema> cines = new HashSet<>(cinemas.values());
        return cines;
    }

    @Override
    public void removeCinema(String name){
        cinemas.remove(name);
    }

    @Override
    public void deleteFunctionByCinemaDateAndMovie(String cinema, String date, String movie) throws CinemaPersistenceException {
        if (cinemas.containsKey(cinema)) {
            cinemas.get(cinema).getFunctions().
                    removeIf(p -> p.getDate().equals(date) && p.getMovie().getName().equals(movie));
        } else {
            throw new CinemaPersistenceException("El cinema especificado no existe");
        }
    }

    

}
