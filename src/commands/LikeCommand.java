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
public final class LikeCommand implements Command {
    /**
     * Likes a movie if it has been liked
     */
    public LikeCommand() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
                Movie movieToLike = currentPage.getCurrentMoviesList().get(0);

                if (currentUser.getWatchedMovies().contains(movieToLike)
                        && !currentUser.getLikedMovies().contains(movieToLike)) {
                    movieToLike.setNumLikes(movieToLike.getNumLikes() + 1);

                    currentUser.getLikedMovies().add(movieToLike);

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
