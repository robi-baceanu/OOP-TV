import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.Input;
import fileio.MovieInput;
import fileio.UserInput;
import platform.Movie;
import platform.MoviesDatabase;
import platform.User;
import platform.UsersDatabase;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        Input inputData = OBJECT_MAPPER.readValue(new File(args[0]), Input.class);

        UsersDatabase.getInstance().initDatabase(inputData);
        MoviesDatabase.getInstance().initDatabase(inputData);

        UsersDatabase.deleteInstance();
        MoviesDatabase.deleteInstance();
    }
}
