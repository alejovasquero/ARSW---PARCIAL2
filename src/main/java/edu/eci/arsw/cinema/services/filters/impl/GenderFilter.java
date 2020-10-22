package edu.eci.arsw.cinema.services.filters.impl;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.services.filters.CinemaFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenderFilter implements CinemaFilter {

    @Override
    public <E> List<Movie> apply(List<CinemaFunction> cf, E filter) {
        List<Movie> lis= new ArrayList<Movie>();
        for(CinemaFunction c: cf){
            if(c.getMovie().getGenre().equals(filter)){
                lis.add(c.getMovie());
            }
        }
        return lis;
    }
}
