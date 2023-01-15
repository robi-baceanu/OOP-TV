package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.Page;
import pages.UpgradesPage;
import platform.App;
import platform.MagicNumbers;
import platform.OutputParser;
import platform.User;

/**
 * @author wh1ter0se
 */
public final class BuyTokensCommand implements Command {
    private final int quantity;

    /**
     * Converts balance to tokens
     *
     * @param quantity number of tokens to be bought
     */
    public BuyTokensCommand(final int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
        if (currentPage instanceof UpgradesPage) {
            User currentUser = App.getInstance().getCurrentUser();
            int currentBalance = currentUser.getCredentials().getCredentials().getBalance();
            int currentTokensCount = currentUser.getTokensCount();

            if (quantity <= currentBalance) {
                currentUser.setTokensCount(currentTokensCount + quantity);
                currentUser.getCredentials().getCredentials().setBalance(currentBalance - quantity);
            } else {
                System.out.println("Eroare de la valori");
                ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        } else {
            System.out.println("Eroare de la pagina");
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }
}
