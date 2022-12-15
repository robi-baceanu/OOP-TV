import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;
import fileio.Credentials;
import fileio.Input;
import pages.Page;
import platform.MagicNumbers;
import platform.App;
import platform.UsersDatabase;
import platform.MoviesDatabase;
import platform.ActionsParser;
import platform.OutputParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class Main {
    private static File resultFile = null;

    private Main() {

    }

    public static void main(final String[] args) throws IOException {
        Input inputData = MagicNumbers.OBJECT_MAPPER.readValue(new File(args[0]), Input.class);

        App.getInstance().initApp();
        UsersDatabase.getInstance().initDatabase(inputData);
        MoviesDatabase.getInstance().initDatabase(inputData);

        ArrayNode output = MagicNumbers.OBJECT_MAPPER.createArrayNode();

        for (ActionInput action : inputData.getActions()) {
            Page currentPage = App.getInstance().getCurrentPage();

            if (action.getType().equals("change page")) {
                if (!action.getPage().equals("see details")) {
                    ActionsParser.changePage(action.getPage(), output);
                } else {
                    String movie = action.getMovie();
                    ActionsParser.changeToDetailsPage(currentPage, movie, action, output);

                    Page newPage = App.getInstance().getCurrentPage();
                    ActionsParser.details(newPage, movie, output);
                }
            } else if (action.getType().equals("on page")) {
                switch (action.getFeature()) {
                    case "login" -> {
                        String nextPage = action.getPage();
                        String name = action.getCredentials().getName();
                        String password = action.getCredentials().getPassword();

                        ActionsParser.login(currentPage, nextPage, name, password, output);
                    }
                    case "register" -> {
                        String nextPage = action.getPage();
                        Credentials credentials = action.getCredentials();

                        ActionsParser.register(currentPage, nextPage, credentials, output);
                    }
                    case "search" -> {
                        String startsWith = action.getStartsWith();

                        ActionsParser.search(currentPage, startsWith, output);
                    }
                    case "filter" -> {
                        String rating = null;
                        String duration = null;

                        if (action.getFilters().getSort() != null) {
                            rating = action.getFilters().getSort().getRating();
                            duration = action.getFilters().getSort().getDuration();
                        }

                        ArrayList<String> actors = null;
                        ArrayList<String> genres = null;

                        if (action.getFilters().getContains() != null) {
                            if (action.getFilters().getContains().getActors() != null) {
                                actors = new ArrayList<>(action.getFilters().
                                        getContains().getActors());
                            }
                            if (action.getFilters().getContains().getGenre() != null) {
                                genres = new ArrayList<>(action.getFilters().
                                        getContains().getGenre());
                            }
                        }

                        ActionsParser.filter(currentPage, rating, duration, actors, genres, output);
                    }
                    case "buy tokens" -> {
                        int count = action.getCount();

                        ActionsParser.buyTokens(currentPage, count, output);
                    }
                    case "buy premium account" -> {
                        ActionsParser.buyPremiumAccount(currentPage, output);
                    }
                    case "purchase" -> {
                        ActionsParser.purchase(currentPage, output);
                    }
                    case "watch" -> {
                        ActionsParser.watch(currentPage, output);
                    }
                    case "like" -> {
                        ActionsParser.like(currentPage, output);
                    }
                    case "rate" -> {
                        double rate = action.getRate();

                        ActionsParser.rate(currentPage, rate, output);
                    }
                    default -> {
                        ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                        OutputParser.createErrorNode(toSend);
                        output.add(toSend);
                    }
                }
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
