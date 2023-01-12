package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Credentials;
import pages.DetailsPage;
import pages.Page;
import platform.*;

import static platform.ActionsParser.showPage;

/**
 * @author wh1ter0se
 */
public final class PurchaseCommand implements Command {
    /**
     * Purchases a movie if it has not been already purchased
     */
    public PurchaseCommand() {

    }

    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
                Movie movieToPurchase = currentPage.getCurrentMoviesList().get(0);
                if (!currentUser.getPurchasedMovies().contains(movieToPurchase)) {
                    Credentials currentUserCredentials = currentUser.
                            getCredentials().getCredentials();
                    if (currentUserCredentials.getAccountType().equals("premium")
                            && currentUser.getNumFreePremiumMovies() > 0) {
                        currentUser.setNumFreePremiumMovies(
                                currentUser.getNumFreePremiumMovies() - 1);
                        currentUser.getPurchasedMovies().add(movieToPurchase);
                        showPage(output);
                    } else {
                        if (currentUser.getTokensCount() >= MagicNumbers.MOVIE_PRICE) {
                            currentUser.setTokensCount(
                                    currentUser.getTokensCount() - MagicNumbers.MOVIE_PRICE);

                            currentUser.getPurchasedMovies().add(movieToPurchase);

                            showPage(output);
                        } else {
                            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                            OutputParser.createErrorNode(toSend);
                            output.add(toSend);
                        }
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
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }
}
