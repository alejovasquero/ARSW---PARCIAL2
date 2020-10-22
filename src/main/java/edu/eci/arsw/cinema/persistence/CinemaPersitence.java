/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import java.util.List;
import java.util.Set;

/**
 *
 * @author cristian
 */
public interface CinemaPersitence {
    
    /**
     * 
     * @param row the row of the seat
     * @param col the column of the seat
     * @param cinema the cinema's name
     * @param date the date of the function
     * @param movieName the name of the movie
     * 
     * @throws CinemaException if the seat is occupied,
     *    or any other low-level persistence error occurs.
     */
    public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException;
    
    /**
     * 
     * @param cinema cinema's name
     * @param date date
     * @return the list of the functions of the cinema in the given date
     */
    public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date) throws CinemaPersistenceException;

    /**
     *
     * @param cinema cinema's name
     * @param date date
     * @return the list of the functions of the cinema in the given date (is specified the dat, month, and year)
     */
    public List<CinemaFunction> getFunctionsbyCinemaAndExactDay(String cinema, String date) throws CinemaPersistenceException;


    /**
     *
     * @param cinema cinema's name
     * @param date date
     * @param movie Movie's name
     * @return the list of the functions of the cinema in the given date, with a specified movie
     */
    public CinemaFunction getFunctionbyCinemaDateAndMovie(String cinema, String date, String movie) throws CinemaPersistenceException;


    /**
     * 
     * @param cinema new cinema
     * @throws  CinemaPersistenceException n if a cinema with the same name already exists
     */
    public void saveCinema(Cinema cinema) throws CinemaPersistenceException;
    
    /**
     * 
     * @param cinema new Function
     * @throws  CinemaPersistenceException n if a cinema function with the same name already exists
     */
    public void saveCinemaFunction(String cinema,CinemaFunction cFunction) throws CinemaPersistenceException;
    
    /**
     * 
     * @param name name of the cinema
     * @return Cinema of the given name
     * @throws  CinemaPersistenceException if there is no such cinema
     */
    public Cinema getCinema(String name) throws CinemaPersistenceException;

    /**
     * Return all cinemas
     * @return Cinemas
     */
	public Set<Cinema> getCinemas();

    /**
     * Deletes a Cinema
     * @param name Name of the cinema
     */
	public void removeCinema(String name) ;

    /**
     * Deletes the function with the specified parameters
     * @param cinema Name of the cinema
     * @param date Date of the function
     * @param movie Name of the movie
     */
	public void deleteFunctionByCinemaDateAndMovie(String cinema, String date, String movie) throws CinemaPersistenceException;
}
