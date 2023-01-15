package platform;

import fileio.Input;
import fileio.MovieInput;

import java.util.ArrayList;

/**
 * Class that stores all movies available on the platform
 *
 * @author wh1ter0se
 */
public final class MoviesDatabase implements Database {
    private static MoviesDatabase moviesDatabase = null;
    private ArrayList<Movie> movies;

    private MoviesDatabase() {
        this.movies = new ArrayList<>();
    }

    /**
     * Method for returning instance of the movies' database
     * (implemented following Singleton pattern)
     *
     * @return instance of movies' database
     */
    public static MoviesDatabase getInstance() {
        if (moviesDatabase == null) {
            moviesDatabase = new MoviesDatabase();
        }
        return moviesDatabase;
    }

    /**
     * Deletes instance of the application,
     * setting its MoviesDatabase field to null
     */
    public static void deleteInstance() {
        moviesDatabase = null;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(final ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public void initDatabase(final Input inputData) {
        for (MovieInput movie : inputData.getMovies()) {
            movies.add(new Movie(movie));
        }
    }

    /**
     * Method that notifies all users of a certain event
     *
     * @param eventType whether a movie was added or deleted
     * @param movie movie that was added / deleted
     */
    public static void notifySubscribers(String eventType, MovieInput movie) {
        for (User user : UsersDatabase.getInstance().getUsers()) {
            user.update(eventType, movie);
        }
    }
}
