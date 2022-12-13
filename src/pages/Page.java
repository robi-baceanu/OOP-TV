package pages;

import platform.App;
import platform.Movie;

import java.util.ArrayList;

public abstract class Page {
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

    public abstract void changePage(String nextPage);
}
