package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.MoviesPage;
import pages.Page;
import platform.*;

import java.util.ArrayList;

/**
 * @author wh1ter0se
 */
public final class SearchCommand implements Command {
    private final String startsWith;

    /**
     * Displays movies that begin with a certain sequence
     *
     * @param startsWith sequence to be searched
     */
    public SearchCommand(final String startsWith) {
        this.startsWith = startsWith;
    }

    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();

        if (currentPage instanceof MoviesPage) {
            ArrayList<Movie> moviesToSearch = new ArrayList<>(currentPage.getCurrentMoviesList());
            moviesToSearch.removeIf(
                    movie -> !movie.getMovieInfo().getName().startsWith(startsWith));
            currentPage.setCurrentMoviesList(moviesToSearch);

            User currentUser = App.getInstance().getCurrentUser();
            OutputParser.createNonErrorNode(toSend, currentUser, currentPage);
        } else {
            OutputParser.createErrorNode(toSend);
        }

        output.add(toSend);

        currentPage.setCurrentMoviesList(App.getInstance().getCurrentUserMovies());
    }
}
