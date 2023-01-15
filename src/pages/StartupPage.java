package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import platform.App;
import platform.MagicNumbers;
import platform.OutputParser;

/**
 * Page where no user is authenticated
 *
 * @author wh1ter0se
 */
public final class StartupPage extends Page {
    public StartupPage() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePage(final String nextPage, final ArrayNode output) {
        if (nextPage.equals("login")
                || nextPage.equals("register")
                || nextPage.equals("logout")) {
            App.getInstance().updateApp(null, nextPage);
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }
}
