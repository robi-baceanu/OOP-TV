package platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Credentials;
import fileio.UserInput;
import pages.LoginPage;
import pages.Page;
import pages.RegisterPage;

public class ActionsParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ActionsParser() {

    }

    public static void changePage(String nextPage, ArrayNode output) {
        App.getInstance().getCurrentPage().changePage(nextPage, output);
    }

    public static void login(Page currentPage, String nextPage, String name, String password, ArrayNode output) {
        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();

        if (currentPage instanceof LoginPage) {
            ((LoginPage) currentPage).setName(name);
            ((LoginPage) currentPage).setPassword(password);
            currentPage.changePage(nextPage, output);

            User currentUser = App.getInstance().getCurrentUser();

            if (currentUser != null) {
                toSend.set("error", null);
                ArrayNode currentMoviesNode = OBJECT_MAPPER.createArrayNode();
                OutputParser.createMoviesArrayNode(currentMoviesNode, currentPage.getCurrentMoviesList());
                toSend.set("currentMoviesList", currentMoviesNode);
                ObjectNode currentUserNode = OBJECT_MAPPER.createObjectNode();
                OutputParser.createCurrentUserNode(currentUser, currentUserNode);
                toSend.set("currentUser", currentUserNode);
            } else {
                OutputParser.createErrorNode(toSend);
            }
        } else {
            OutputParser.createErrorNode(toSend);
        }

        output.add(toSend);
    }

    public static void register(Page currentPage, String nextPage, Credentials credentials, ArrayNode output) {
        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();

        if (currentPage instanceof RegisterPage) {
            UserInput userCredentials = new UserInput();
            userCredentials.setCredentials(credentials);
            ((RegisterPage) currentPage).setCredentials(userCredentials);
            currentPage.changePage(nextPage, output);

            User currentUser = App.getInstance().getCurrentUser();
            if (currentUser != null) {
                toSend.set("error", null);
                ArrayNode currentMoviesNode = OBJECT_MAPPER.createArrayNode();
                OutputParser.createMoviesArrayNode(currentMoviesNode, currentPage.getCurrentMoviesList());
                toSend.set("currentMoviesList", currentMoviesNode);
                ObjectNode currentUserNode = OBJECT_MAPPER.createObjectNode();
                OutputParser.createCurrentUserNode(currentUser, currentUserNode);
                toSend.set("currentUser", currentUserNode);
            } else {
                OutputParser.createErrorNode(toSend);
            }
        } else {
            OutputParser.createErrorNode(toSend);
        }

        output.add(toSend);
    }
}
