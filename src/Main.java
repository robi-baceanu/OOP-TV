import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Invoker;
import fileio.ActionInput;
import fileio.Input;
import pages.Page;
import platform.MagicNumbers;
import platform.App;
import platform.UsersDatabase;
import platform.MoviesDatabase;
import platform.ActionsParser;

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

            if (action.getType().equals("change page")) {
                if (!action.getPage().equals("see details")) {
                    ActionsParser.changePage(action.getPage(), output);
                } else {
                    String movie = action.getMovie();
                    ActionsParser.changeToDetailsPage(currentPage, movie, action, output);
                }
            } else if (action.getType().equals("on page")) {
                ActionsParser.executeCommand(action.getFeature(), action,
                        currentPage, output, invoker);
            }
        }

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
