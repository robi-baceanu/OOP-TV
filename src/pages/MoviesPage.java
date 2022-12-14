package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import platform.App;

public class MoviesPage extends Page {
    public MoviesPage() {
        super();
        this.setCurrentMoviesList(App.getInstance().getCurrentUserMovies());
    }

    @Override
    public void changePage(String nextPage, ArrayNode output) {

    }
}
