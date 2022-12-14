package platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Credentials;
import fileio.UserInput;
import pages.LoginPage;
import pages.MoviesPage;
import pages.Page;
import pages.RegisterPage;

import java.util.Comparator;

public class ActionsParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ActionsParser() {

    }

    public static void changePage(String nextPage, ArrayNode output) {
        App.getInstance().getCurrentPage().changePage(nextPage, output);

        if (nextPage.equals("movies")) {
            showPage(output);
        }
    }

    public static void showPage(ArrayNode output) {
        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();

        User currentUser = App.getInstance().getCurrentUser();
        Page currentPage = App.getInstance().getCurrentPage();

        OutputParser.createNonErrorNode(toSend, currentUser, currentPage);

        output.add(toSend);
    }


    public static void login(Page currentPage, String nextPage, String name, String password, ArrayNode output) {
        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();

        if (currentPage instanceof LoginPage) {
            ((LoginPage) currentPage).setName(name);
            ((LoginPage) currentPage).setPassword(password);
            currentPage.changePage(nextPage, output);

            User currentUser = App.getInstance().getCurrentUser();

            if (currentUser != null) {
                OutputParser.createNonErrorNode(toSend, currentUser, currentPage);
            } else {
                OutputParser.createErrorNode(toSend);
            }
        } else {
            OutputParser.createErrorNode(toSend);
        }

        output.add(toSend);
    }

    public static void register(Page currentPage, String nextPage, Credentials credentials, ArrayNode output) {
        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();

        if (currentPage instanceof RegisterPage) {
            UserInput userCredentials = new UserInput();
            userCredentials.setCredentials(credentials);
            ((RegisterPage) currentPage).setCredentials(userCredentials);
            currentPage.changePage(nextPage, output);

            User currentUser = App.getInstance().getCurrentUser();
            if (currentUser != null) {
                OutputParser.createNonErrorNode(toSend, currentUser, currentPage);
            } else {
                OutputParser.createErrorNode(toSend);
            }
        } else {
            OutputParser.createErrorNode(toSend);
        }

        output.add(toSend);
    }

    public static void search(Page currentPage, String startsWith, ArrayNode output) {
        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();

        if (currentPage instanceof MoviesPage) {
            currentPage.getCurrentMoviesList().removeIf(movie -> !movie.getMovieInfo().getName().startsWith(startsWith));

            User currentUser = App.getInstance().getCurrentUser();
            OutputParser.createNonErrorNode(toSend, currentUser, currentPage);
        } else {
            OutputParser.createErrorNode(toSend);
        }

        output.add(toSend);
    }

    public static void filter(Page currentPage, String rating, String duration, ArrayNode output) {
        if (currentPage instanceof MoviesPage) {
            int ratingSort = 0;
            int durationSort = 0;

            if (rating != null) {
                if (rating.equals("increasing"))
                    ratingSort = 1;
                else if (rating.equals("decreasing"))
                    ratingSort = -1;
            }

            if (duration != null) {
                if (duration.equals("increasing"))
                    durationSort = 1;
                else if (duration.equals("decreasing"))
                    durationSort = -1;
            }

            int finalDurationSort = durationSort;
            int finalRatingSort = ratingSort;

            currentPage.getCurrentMoviesList().sort(new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    if (finalDurationSort != 0) {
                        int durationMovie1 = o1.getMovieInfo().getDuration();
                        int durationMovie2 = o2.getMovieInfo().getDuration();
                        if (durationMovie1 == durationMovie2) {
                            if (finalRatingSort != 0) {
                                double ratingMovie1 = o1.getRating();
                                double ratingMovie2 = o2.getRating();
                                return finalRatingSort * Double.compare(ratingMovie1, ratingMovie2);
                            } else {
                                return 0;
                            }
                        } else {
                            return finalDurationSort * (durationMovie2 - durationMovie1);
                        }
                    } else {
                        double ratingMovie1 = o1.getRating();
                        double ratingMovie2 = o2.getRating();
                        if (finalRatingSort != 0) {
                            return finalRatingSort * Double.compare(ratingMovie1, ratingMovie2);
                        } else {
                            return 0;
                        }
                    }
                }
            });

            showPage(output);
        } else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }
}
