package edu.eci.arsw.cinema.services.filters;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;

import java.util.List;
import java.util.function.BiFunction;

public interface CinemaFilter{
    public <E> List<Movie> apply(List<CinemaFunction> cf, E filter);
}
