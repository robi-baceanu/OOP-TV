package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.DetailsPage;
import pages.Page;
import platform.*;

/**
 * @author wh1ter0se
 */
public final class SubscribeCommand implements Command {
    private final String subscribedGenre;

    /**
     * Subscribes user to a genre, if not already subscribed to it
     *
     * @param subscribedGenre genre to subscribe to
     */
    public SubscribeCommand(final String subscribedGenre) {
        this.subscribedGenre = subscribedGenre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            Movie movie = currentPage.getCurrentMoviesList().get(0);

            if (movie.getMovieInfo().getGenres().contains(subscribedGenre)) {
                if (!currentUser.getSubscriptions().contains(subscribedGenre)) {
                    currentUser.getSubscriptions().add(subscribedGenre);
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
