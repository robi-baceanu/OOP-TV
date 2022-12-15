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
import java.util.ArrayList;

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
//        for (int i = 10; i <= 10; i++) {
//            Input inputData = OBJECT_MAPPER.readValue(new File("checker/resources/in/basic_" + i + ".json"), Input.class);
            Input inputData = OBJECT_MAPPER.readValue(new File(args[0]), Input.class);

            App.getInstance().initApp();
            UsersDatabase.getInstance().initDatabase(inputData);
            MoviesDatabase.getInstance().initDatabase(inputData);

            ArrayNode output = OBJECT_MAPPER.createArrayNode();

            for (ActionInput action : inputData.getActions()) {
                Page currentPage = App.getInstance().getCurrentPage();
//                System.out.println(currentPage);

                if (action.getType().equals("change page")) {
                    if (!action.getPage().equals("see details"))
                        ActionsParser.changePage(action.getPage(), output);
                    else {
//                        ActionsParser.changePage(action.getPage(), output);
//                        String movie = action.getMovie();
//
//                        ActionsParser.details(currentPage, movie, output);
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

//                            System.out.println(currentPage.getCurrentMoviesList());
//                            System.out.println(App.getInstance().getCurrentUserMovies());
                            ActionsParser.search(currentPage, startsWith, output);
//                            System.out.println(currentPage.getCurrentMoviesList());
//                            System.out.println(App.getInstance().getCurrentUserMovies());
                            currentPage.setCurrentMoviesList(App.getInstance().getCurrentUserMovies());
//                            System.out.println(currentPage.getCurrentMoviesList());
//                            System.out.println(App.getInstance().getCurrentUserMovies());
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
                                    actors = new ArrayList<>(action.getFilters().getContains().getActors());
//                                    System.out.println(actors);
                                }
                                if (action.getFilters().getContains().getGenre() != null) {
                                    genres = new ArrayList<>(action.getFilters().getContains().getGenre());
//                                    System.out.println(genres);
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
                            String movie = action.getMovie();

                            ActionsParser.purchase(currentPage, movie, output);
                        }
                        case "watch" -> {
                            String movie = action.getMovie();

                            ActionsParser.watch(currentPage, movie, output);
                        }
                        case "like" -> {
                            String movie = action.getMovie();

                            ActionsParser.like(currentPage, movie, output);
                        }
                        case "rate" -> {
                            String movie = action.getMovie();
                            double rate = action.getRate();

                            ActionsParser.rate(currentPage, movie, rate, output);
                        }
                    }
                }
            }

            ObjectWriter objectWriter = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

        if (Main.resultFile != null) {
            Main.resultFile.delete();
        }

//            resultFile = new File("checker/resources/out/basic_" + i + "out.json");
            resultFile = new File(args[1]);
            objectWriter.writeValue(resultFile, output);

            App.deleteInstance();
            UsersDatabase.deleteInstance();
            MoviesDatabase.deleteInstance();
//        }
    }
}
