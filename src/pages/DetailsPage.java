package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import platform.App;
import platform.OutputParser;
import platform.User;

public class DetailsPage extends Page {
    public DetailsPage() {
        super();
    }

    @Override
    public void changePage(String nextPage, ArrayNode output) {
        if (nextPage.equals("movies") || nextPage.equals("upgrades") || nextPage.equals("homepage")) {
            User currentUser = App.getInstance().getCurrentUser();
            App.getInstance().updateApp(currentUser, nextPage);
        } else if (nextPage.equals("logout")) {
            App.getInstance().updateApp(null, nextPage);
        } else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }
}
