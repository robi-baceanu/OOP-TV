package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Credentials;
import pages.Page;
import pages.UpgradesPage;
import platform.App;
import platform.MagicNumbers;
import platform.OutputParser;
import platform.User;

/**
 * @author wh1ter0se
 */
public final class BuyPremiumAccountCommand implements Command {
    /**
     * Converts the account to a premium one
     */
    public BuyPremiumAccountCommand() {

    }

    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        if (currentPage instanceof UpgradesPage) {
            User currentUser = App.getInstance().getCurrentUser();
            Credentials currentUserCredentials = currentUser.getCredentials().getCredentials();

            if (currentUserCredentials.getAccountType().equals("standard")
                    && currentUser.getTokensCount() >= MagicNumbers.PREMIUM_ACCOUNT_PRICE) {
                currentUserCredentials.setAccountType("premium");
                currentUser.setTokensCount(
                        currentUser.getTokensCount() - MagicNumbers.PREMIUM_ACCOUNT_PRICE);
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
