import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionInput;
import fileio.Credentials;
import fileio.Input;
import pages.Page;
import platform.ActionsParser;
import platform.App;
import platform.MoviesDatabase;
import platform.UsersDatabase;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static File resultFile = null;

    public static File getResultFile() {
        return resultFile;
    }

    public static void setResultFile(File resultFile) {
        Main.resultFile = resultFile;
    }

    public static void main(String[] args) throws IOException {
//        Input inputData = OBJECT_MAPPER.readValue(new File("checker/resources/in/basic_" + 1 + ".json"), Input.class);
        Input inputData = OBJECT_MAPPER.readValue(new File(args[0]), Input.class);

        App.getInstance().initApp();
        UsersDatabase.getInstance().initDatabase(inputData);
        MoviesDatabase.getInstance().initDatabase(inputData);

        ArrayNode output = OBJECT_MAPPER.createArrayNode();

        for (ActionInput action : inputData.getActions()) {
            Page currentPage = App.getInstance().getCurrentPage();
//            System.out.println(currentPage);

            if (action.getType().equals("change page")) {
                ActionsParser.changePage(action.getPage(), output);
            } else if (action.getType().equals("on page")) {
                if (action.getFeature().equals("login")) {
                    String nextPage = action.getPage();
                    String name = action.getCredentials().getName();
                    String password = action.getCredentials().getPassword();

                    ActionsParser.login(currentPage, nextPage, name, password, output);
                } else if (action.getFeature().equals("register")) {
                    String nextPage = action.getPage();
                    Credentials credentials = action.getCredentials();

                    ActionsParser.register(currentPage, nextPage, credentials, output);
                }
            }
        }

        ObjectWriter objectWriter = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

        if (Main.resultFile != null) {
            Main.resultFile.delete();
        }

//        resultFile = new File("checker/resources/out/basic_" + 1 + "out.json");
        resultFile = new File(args[1]);
        objectWriter.writeValue(resultFile, output);

        App.deleteInstance();
        UsersDatabase.deleteInstance();
        MoviesDatabase.deleteInstance();
    }
}
