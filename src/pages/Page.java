package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import platform.Movie;

import java.util.ArrayList;

/**
 * Abstract class that describes a page
 *
 * @author wh1ter0se
 */
public abstract class Page {
    private ArrayList<Movie> currentMoviesList;

    public Page() {
        currentMoviesList = new ArrayList<>();
    }

    public final ArrayList<Movie> getCurrentMoviesList() {
        return currentMoviesList;
    }

    public final void setCurrentMoviesList(final ArrayList<Movie> currentMoviesList) {
        this.currentMoviesList = currentMoviesList;
    }

    /**
     * Method that changes the page of the app to the next one,
     * by returning instance of the next page or producing an
     * error if the action is not allowed
     *
     * @param nextPage name of the next page
     * @param output ArrayNode where output is passed
     */
    public abstract void changePage(String nextPage, ArrayNode output);
}
