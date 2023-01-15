import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Invoker;
import fileio.ActionInput;
import fileio.Input;
import fileio.MovieInput;
import pages.Page;
import platform.*;

import java.io.File;
import java.io.IOException;

public final class Main {
    private static File resultFile = null;

    private Main() {

    }

    /**
     * Entry point of the platform
     *
     * @param args paths to input / output files
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        Input inputData = MagicNumbers.OBJECT_MAPPER.readValue(new File(args[0]), Input.class);

        App.getInstance().initApp();
        UsersDatabase.getInstance().initDatabase(inputData);
        MoviesDatabase.getInstance().initDatabase(inputData);

        ArrayNode output = MagicNumbers.OBJECT_MAPPER.createArrayNode();

        Invoker invoker = new Invoker();

        for (ActionInput action : inputData.getActions()) {
            Page currentPage = App.getInstance().getCurrentPage();

            switch (action.getType()) {
                case "change page" -> {
                    if (App.getInstance().getCurrentUser() != null) {
                        App.getInstance().getCurrentUser().
                                getAccessedPages().addLast(action.getPage());
                    }

                    if (!action.getPage().equals("see details")) {
                        ActionsParser.changePage(action.getPage(), output);
                    } else {
                        String movie = action.getMovie();
                        ActionsParser.changeToDetailsPage(currentPage, movie, action, output);
                    }
                }

                case "on page" -> ActionsParser.executeCommand(action.getFeature(), action,
                        currentPage, output, invoker);

                case "back" -> ActionsParser.back(output);

                case "database" -> {
                    if (action.getFeature().equals("add")) {
                        MovieInput movieToAdd = action.getAddedMovie();
                        ActionsParser.databaseAdd(movieToAdd, output);
                    } else if (action.getFeature().equals("delete")) {
                        String movieToDelete = action.getDeletedMovie();
                        ActionsParser.databaseDelete(movieToDelete, output);
                    }
                }

                default -> {
                    ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                    OutputParser.createErrorNode(toSend);
                    output.add(toSend);
                }
            }
        }

        ActionsParser.launchRecommendation(output);

        ObjectWriter objectWriter = MagicNumbers.OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

        if (Main.resultFile != null) {
            Main.resultFile.delete();
        }

        resultFile = new File(args[1]);
        objectWriter.writeValue(resultFile, output);

        App.deleteInstance();
        UsersDatabase.deleteInstance();
        MoviesDatabase.deleteInstance();
    }
}
