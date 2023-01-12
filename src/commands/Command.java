package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import pages.Page;

/**
 * Interface used for implementing "on page" commands
 *
 * @author wh1ter0se
 */
public interface Command {
    /**
     * Executes "on page" command
     *
     * @param currentPage instance of current page
     * @param output ArrayNode where output is passed
     */
    void execute(Page currentPage, ArrayNode output);
}
