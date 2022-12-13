package pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import platform.Movie;

import java.util.ArrayList;

public abstract class Page {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private ArrayList<Movie> currentMoviesList;

    public Page() {
        currentMoviesList = new ArrayList<>();
    }

    public ArrayList<Movie> getCurrentMoviesList() {
        return currentMoviesList;
    }

    public void setCurrentMoviesList(ArrayList<Movie> currentMoviesList) {
        this.currentMoviesList = currentMoviesList;
    }

    public abstract void changePage(String nextPage, ArrayNode output);
}
