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
public final class WatchCommand implements Command {
    /**
     * Watches a movie if it has been purchased
     */
    public WatchCommand() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
                Movie movieToWatch = currentPage.getCurrentMoviesList().get(0);

                if (currentUser.getPurchasedMovies().contains(movieToWatch)) {
                    if (!currentUser.getWatchedMovies().contains(movieToWatch)) {
                        currentUser.getWatchedMovies().add(movieToWatch);
                    }
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
