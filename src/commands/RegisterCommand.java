package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Credentials;
import fileio.UserInput;
import pages.Page;
import pages.RegisterPage;
import platform.App;
import platform.MagicNumbers;
import platform.OutputParser;
import platform.User;

/**
 * @author wh1ter0se
 */
public final class RegisterCommand implements Command {
    private final String nextPage;
    private final Credentials credentials;

    /**
     * Provides register credentials and creates account if one didn't previously
     * exist, or produces an error otherwise
     *
     * @param nextPage name of the next page
     * @param credentials credentials provided for creating account
     */
    public RegisterCommand(final String nextPage, final Credentials credentials) {
        this.nextPage = nextPage;
        this.credentials = credentials;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();

        if (currentPage instanceof RegisterPage) {
            UserInput userCredentials = new UserInput();
            userCredentials.setCredentials(credentials);
            ((RegisterPage) currentPage).setCredentials(userCredentials);
            currentPage.changePage(nextPage, output);

            User currentUser = App.getInstance().getCurrentUser();
            if (currentUser != null) {
                OutputParser.createNonErrorNode(toSend, currentUser, currentPage);
            } else {
                OutputParser.createErrorNode(toSend);
            }
        } else {
            OutputParser.createErrorNode(toSend);
        }

        output.add(toSend);
    }
}
