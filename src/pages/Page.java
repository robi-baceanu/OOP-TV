package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import platform.Movie;

import java.util.ArrayList;

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

    public abstract void changePage(String nextPage, ArrayNode output);
}
