package platform;

import fileio.Input;
import fileio.MovieInput;

import java.util.ArrayList;

public class MoviesDatabase implements Database {
    private static MoviesDatabase moviesDatabase = null;
    private ArrayList<Movie> movies;

    private MoviesDatabase() {
        this.movies = new ArrayList<Movie>();
    }

    public static MoviesDatabase getInstance() {
        if (moviesDatabase== null) {
            moviesDatabase = new MoviesDatabase();
        }
        return moviesDatabase;
    }

    public static void deleteInstance() {
        moviesDatabase = null;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public void initDatabase(Input inputData) {
        for (MovieInput movie : inputData.getMovies()) {
            movies.add(new Movie(movie));
        }
    }
}
