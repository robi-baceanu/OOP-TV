import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.Input;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        Input inputData = OBJECT_MAPPER.readValue(new File(args[0]), Input.class);
        System.out.println(inputData);
    }
}
