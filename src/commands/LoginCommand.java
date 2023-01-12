package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.LoginPage;
import pages.Page;
import platform.App;
import platform.MagicNumbers;
import platform.OutputParser;
import platform.User;

/**
 * @author wh1ter0se
 */
public final class LoginCommand implements Command {
    private final String nextPage;
    private final String name;
    private final String password;

    /**
     * Provides log-in credentials and logs user in if credentials
     * are correct, or produces an error otherwise
     *
     * @param nextPage name of the next page
     * @param name name provided for log-in
     * @param password password provided for log-in
     */
    public LoginCommand(final String nextPage, final String name, final String password) {
        this.nextPage = nextPage;
        this.name = name;
        this.password = password;
    }

    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();

        if (currentPage instanceof LoginPage) {
            ((LoginPage) currentPage).setName(name);
            ((LoginPage) currentPage).setPassword(password);
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
