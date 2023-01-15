package platform;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.*;
import fileio.ActionInput;
import fileio.Credentials;
import fileio.MovieInput;
import pages.Page;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Utility class for implementing actions
 *
 * @author wh1ter0se
 */
public final class ActionsParser {
    private ActionsParser() {

    }

    /**
     * Displays the contents of a page, or error if there is
     * no user logged in
     *
     * @param output ArrayNode where output is passed
     */
    public static void showPage(final ArrayNode output) {
        ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();

        User currentUser = App.getInstance().getCurrentUser();
        Page currentPage = App.getInstance().getCurrentPage();

        if (currentUser != null) {
            OutputParser.createNonErrorNode(toSend, currentUser, currentPage);
        } else {
            OutputParser.createErrorNode(toSend);
        }
        output.add(toSend);
    }

    /**
     * Changes the current page of the app
     *
     * @param nextPage name of the next page
     * @param output ArrayNode where output is passed
     */
    public static void changePage(final String nextPage, final ArrayNode output) {
        App.getInstance().getCurrentPage().changePage(nextPage, output);

        if (nextPage.equals("movies")) {
            showPage(output);
        }
    }

    /**
     * Attempts changing the page to a "See details" page
     *
     * @param currentPage instance of current page
     * @param movie name of movie to display details for
     * @param action current action being parsed
     * @param output ArrayNode where output is passed
     */
    public static void changeToDetailsPage(final Page currentPage, final String movie,
                                           final ActionInput action, final ArrayNode output) {
        boolean movieToDetailExists = false;

        for (Movie movieInstance : currentPage.getCurrentMoviesList()) {
            if (movieInstance != null && movieInstance.getMovieInfo().getName().equals(movie)) {
                movieToDetailExists = true;
                break;
            }
        }

        if (movieToDetailExists) {
            ActionsParser.changePage(action.getPage(), output);

            Page newPage = App.getInstance().getCurrentPage();

            for (Movie movieInstance : App.getInstance().getCurrentUserMovies()) {
                if (movieInstance != null && movieInstance.getMovieInfo().
                        getName().equals(movie)) {
                    newPage.getCurrentMoviesList().add(movieInstance);
                }
            }

            showPage(output);
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
            App.getInstance().getCurrentUser().getAccessedPages().removeLast();
        }
    }

    /**
     * Generates instance of a given command
     *
     * @param commandName type of command to be generated
     * @param action current action being parsed
     * @param output ArrayNode where output is passed
     * @return instance of command that is generated
     */
    private static Command getCommand(final String commandName, final ActionInput action,
                                      final ArrayNode output) {
        switch (commandName) {
            case "login" -> {
                String nextPage = action.getPage();
                String name = action.getCredentials().getName();
                String password = action.getCredentials().getPassword();

                return new LoginCommand(nextPage, name, password);
            }
            case "register" -> {
                String nextPage = action.getPage();
                Credentials credentials = action.getCredentials();

                return new RegisterCommand(nextPage, credentials);
            }
            case "search" -> {
                String startsWith = action.getStartsWith();

                return new SearchCommand(startsWith);
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

                return new FilterCommand(rating, duration, actors, genres);
            }
            case "buy tokens" -> {
                int count = action.getCount();

                return new BuyTokensCommand(count);
            }
            case "buy premium account" -> {
                return new BuyPremiumAccountCommand();
            }
            case "purchase" -> {
                return new PurchaseCommand();
            }
            case "watch" -> {
                return new WatchCommand();
            }
            case "like" -> {
                return new LikeCommand();
            }
            case "rate" -> {
                double rate = action.getRate();

                return new RateCommand(rate);
            }
            case "subscribe" -> {
                String subscribedGenre = action.getSubscribedGenre();

                return new SubscribeCommand(subscribedGenre);
            }
            default -> {
                ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);

                return null;
            }
        }
    }

    /**
     * Executes a given "on page" command
     *
     * @param commandName type of command to be executed
     * @param action current action being parsed
     * @param currentPage instance of current page
     * @param output ArrayNode where output is passed
     * @param invoker instance of invoker that executes commands
     */
    public static void executeCommand(final String commandName, final ActionInput action,
                                      final Page currentPage, final ArrayNode output,
                                      final Invoker invoker) {
        Command command = getCommand(commandName, action, output);
        invoker.execute(command, currentPage, output);
    }

    public static void back(final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentUser != null) {
            if (currentUser.getAccessedPages().size() > 1) {
                LinkedList<String> userAccessedPages = currentUser.getAccessedPages();
                currentUser.getAccessedPages().removeLast();

                if (currentUser.getAccessedPages().size() > 0) {
                    changePage(userAccessedPages.getLast(), output);
                } else {
                    ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                    OutputParser.createErrorNode(toSend);
                    output.add(toSend);
                }
            } else {
                ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        }
    }

    public static void databaseAdd(final MovieInput movieToAdd, final ArrayNode output) {
        boolean movieExistsInDatabase = false;
        String movieName = movieToAdd.getName();

        for (Movie movie : MoviesDatabase.getInstance().getMovies()) {
            if (movie.getMovieInfo().getName().equals(movieName)) {
                movieExistsInDatabase = true;
                break;
            }
        }

        if (!movieExistsInDatabase) {
            MoviesDatabase.getInstance().getMovies().add(new Movie(movieToAdd));
            MoviesDatabase.notifySubscribers("add", movieToAdd);
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    public static void databaseDelete(final String movieToDelete, final ArrayNode output) {
        boolean movieExistsInDatabase = false;
        MovieInput movieToDeleteInput = null;

        for (Movie movie : MoviesDatabase.getInstance().getMovies()) {
            if (movie.getMovieInfo().getName().equals(movieToDelete)) {
                movieExistsInDatabase = true;
                movieToDeleteInput = movie.getMovieInfo();
                break;
            }
        }

        if (movieExistsInDatabase) {
            MoviesDatabase.getInstance().getMovies().removeIf(movie -> movie.getMovieInfo().getName().equals(movieToDelete));
            MoviesDatabase.notifySubscribers("delete", movieToDeleteInput);
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    public static void launchRecommendation(final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        class Genre {
            private String genre;
            private int numLikes;

            public Genre(String genre) {
                this.genre = genre;
                this.numLikes = 1;
            }

            public String getGenre() {
                return genre;
            }

            public void setGenre(String genre) {
                this.genre = genre;
            }

            public int getNumLikes() {
                return numLikes;
            }

            public void setNumLikes(int numLikes) {
                this.numLikes = numLikes;
            }
        }

        if (currentUser != null) {
            if (currentUser.getCredentials().getCredentials().getAccountType().equals("premium")) {
                ArrayList<Genre> topGenres = new ArrayList<>();

                for (Movie movie : currentUser.getLikedMovies()) {
                    for (String genre : movie.getMovieInfo().getGenres()) {
                        boolean genreExistsInTop = false;
                        Genre genreToIncrement = null;

                        for (Genre entry : topGenres) {
                            if (entry.genre.equals(genre)) {
                                genreExistsInTop = true;
                                genreToIncrement = entry;
                                break;
                            }
                        }

                        if (!genreExistsInTop) {
                            topGenres.add(new Genre(genre));
                        } else {
                            genreToIncrement.numLikes++;
                        }
                    }
                }

                topGenres.sort(new Comparator<Genre>() {
                    @Override
                    public int compare(Genre o1, Genre o2) {
                        if (o1.numLikes == o2.numLikes) {
                            return o1.genre.compareTo(o2.genre);
                        } else {
                            return o2.numLikes - o1.numLikes;
                        }
                    }
                });

                ArrayList<Movie> currentUserMovies = new ArrayList<>(App.getInstance().getCurrentUserMovies());
                currentUserMovies.sort(new Comparator<Movie>() {
                    @Override
                    public int compare(Movie o1, Movie o2) {
                        return o2.getNumLikes() - o1.getNumLikes();
                    }
                });

                Movie movieToRecommend = null;
                for (Genre genre : topGenres) {
                    for (Movie movie : currentUserMovies) {
                        if (movie.getMovieInfo().getGenres().contains(genre.getGenre()) &&
                                !currentUser.getWatchedMovies().contains(movie)) {
                            movieToRecommend = movie;
                            break;
                        }
                    }
                }

                if (movieToRecommend != null) {
                    currentUser.getNotifications().add(new Notification(movieToRecommend.getMovieInfo().getName(), "Recommendation"));
                } else {
                    currentUser.getNotifications().add(new Notification("No recommendation", "Recommendation"));
                }

                ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                OutputParser.createRecommendationNode(toSend, currentUser);
                output.add(toSend);
            }
        }
    }
}
