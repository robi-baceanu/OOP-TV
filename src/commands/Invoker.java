package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import pages.Page;

/**
 * Invoker class for executing commands
 *
 * @author wh1ter0se
 */
public final class Invoker {
    /**
     * Executes a given command
     *
     * @param command instance of command to be executed
     * @param currentPage instance of current page
     * @param output ArrayNode where output is passed
     */
    public void execute(final Command command, final Page currentPage, final ArrayNode output) {
        command.execute(currentPage, output);
    }
}
