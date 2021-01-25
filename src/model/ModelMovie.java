package model;

import model.entity.Movie;
import model.repository.RepositoryMovie;

import java.util.ArrayList;

public class ModelMovie {

    private RepositoryMovie repositoryMovie;

    private static ModelMovie instance;

    public static synchronized ModelMovie getInstance() {
        if(instance == null){
            instance = new ModelMovie();
        }
        return instance;
    }

    private ModelMovie() {
        repositoryMovie = new RepositoryMovie();
    }

    public void createMovie(Movie movie) {
        if(repositoryMovie.readMoviesByName(movie.getName()) != null) {
            repositoryMovie.createMovie(movie);
        }
    }

    public Movie readMoviesById(String value) {
        return repositoryMovie.readMoviesById(value);
    }

    public ArrayList<Movie> readMoviesByName(String value) {
        return repositoryMovie.readMoviesByName(value);
    }

    public void deleteMovieByName(Movie movie) {
        if(repositoryMovie.readMoviesByName(movie.getName()) != null) {
            repositoryMovie.deleteMovieByName(movie.getName());
        }
    }

}
