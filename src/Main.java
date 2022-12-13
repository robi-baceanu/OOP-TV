import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.Input;
import platform.App;
import platform.MoviesDatabase;
import platform.UsersDatabase;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        Input inputData = OBJECT_MAPPER.readValue(new File(args[0]), Input.class);

        App.getInstance().initApp();
        UsersDatabase.getInstance().initDatabase(inputData);
        MoviesDatabase.getInstance().initDatabase(inputData);

        App.deleteInstance();
        UsersDatabase.deleteInstance();
        MoviesDatabase.deleteInstance();
    }
}
