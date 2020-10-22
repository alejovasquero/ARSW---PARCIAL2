/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.services;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.eci.arsw.cinema.services.filters.CinemaFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author cristian
 */
@Service
@Scope("prototype")
public class CinemaServices {
    @Autowired
    CinemaPersitence cps=null;

    @Autowired
    CinemaFilter filter;

    public void addNewCinema(Cinema c) throws CinemaException {
        try {
            cps.saveCinema(c);
        } catch (CinemaPersistenceException e) {
            throw new CinemaException("Error al insertar el nuevo cinema");
        }
    }
    
    public Set<Cinema> getAllCinemas(){
        return cps.getCinemas();
    }
    
    /**
     * 
     * @param name cinema's name
     * @return the cinema of the given name created by the given author
     * @throws CinemaException
     */
    public Cinema getCinemaByName(String name) throws CinemaException{
        try {
            return cps.getCinema(name);
        } catch (CinemaPersistenceException e) {
            throw new CinemaException("Error obteniendo el cinema con el nombre dado", e);
        }
    }
    
    
    public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException {
        cps.buyTicket(row, col, cinema, date, movieName);
    }
    
    public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date)throws CinemaException {
        try {
            return cps.getFunctionsbyCinemaAndDate(cinema, date);
        } catch (CinemaPersistenceException e) {
            throw new CinemaException("Error obteniendo el cinema con el nombre especificado");
        }
    }

    public List<CinemaFunction> getFunctionsbyCinemaAndExactDay(String cinema, String date)throws CinemaException {
        try {
            return cps.getFunctionsbyCinemaAndExactDay(cinema, date);
        } catch (CinemaPersistenceException e) {
            throw new CinemaException("Error obteniendo el cinema con el nombre especificado");
        }
    }

    public CinemaFunction getFunctionbyCinemaDateAndMovie(String cinema, String date, String movie) throws CinemaException {
        try {
            return cps.getFunctionbyCinemaDateAndMovie(cinema, date, movie);
        } catch (CinemaPersistenceException e) {
            throw new CinemaException("Error obteniendo el cinema con el nombre especificado");
        }
    }

    public CinemaPersitence getCps(){
        return cps;
    }

    public void setCps(CinemaPersitence cps){
        this.cps = cps;
    }


    public <E> List<Movie> getFilteredMovies(String cinema, String date, E criteria) throws CinemaException{
        ArrayList<CinemaFunction> ava = new ArrayList<CinemaFunction>();
        try {
            for(CinemaFunction cf: getCinemaByName(cinema).getFunctions()){
                if(cf.getDate().equals(date)){
                    ava.add(cf);
                }
            }
        } catch ( NullPointerException e) {
            throw new CinemaException("El Cinema especificado no existe", e);
        }
        return filter.apply(ava, criteria);
    }

    public void setFilter(CinemaFilter c){
        filter = c;
    }
    public CinemaFilter getFilter(){
        return filter;
    }

    public void removeCinema(String name){
        cps.removeCinema(name);
    }

    public void addFuncion(String cinema,CinemaFunction cFunction) throws CinemaException {
        try {
            cps.saveCinemaFunction(cinema,cFunction);
        } catch (CinemaPersistenceException e) {
            throw new CinemaException("Error al insertar la funcion");
        }
    }

    public void deleteFunctionByNameAndDate(String cinema, String date, String movie) throws CinemaPersistenceException {
        cps.deleteFunctionByCinemaDateAndMovie(cinema, date, movie);
    }
}
