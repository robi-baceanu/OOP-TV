package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.DetailsPage;
import pages.Page;
import platform.*;

import static platform.ActionsParser.showPage;

/**
 * @author wh1ter0se
 */
public final class RateCommand implements Command {
    private final double rate;

    /**
     * Rates a movie if it has been watched
     *
     * @param rate rating to be given
     */
    public RateCommand(final double rate) {
        this.rate = rate;
    }

    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
                Movie movieToRate = currentPage.getCurrentMoviesList().get(0);

                if (currentUser.getWatchedMovies().contains(movieToRate)
                        && !currentUser.getRatedMovies().contains(movieToRate)
                        && rate <= MagicNumbers.MAX_RATING) {
                    movieToRate.setSumOfRatings(movieToRate.getSumOfRatings() + rate);
                    movieToRate.setNumRatings(movieToRate.getNumRatings() + 1);
                    movieToRate.setRating(
                            movieToRate.getSumOfRatings() / movieToRate.getNumRatings());

                    currentUser.getRatedMovies().add(movieToRate);

                    showPage(output);
                } else {
                    ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                    OutputParser.createErrorNode(toSend);
                    output.add(toSend);
                }
            } else {
                ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }
}
