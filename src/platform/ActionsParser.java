package platform;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;
import fileio.Credentials;
import fileio.UserInput;
import pages.Page;
import pages.LoginPage;
import pages.RegisterPage;
import pages.MoviesPage;
import pages.DetailsPage;
import pages.UpgradesPage;

import java.util.ArrayList;
import java.util.Comparator;

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
        }
    }

    /**
     * Provides log-in credentials and logs user in if credentials
     * are correct, or produces an error otherwise
     *
     * @param currentPage instance of current page
     * @param nextPage name of the next page
     * @param name name provided for log-in
     * @param password password provided for log-in
     * @param output ArrayNode where output is passed
     */
    public static void login(final Page currentPage, final String nextPage, final String name,
                             final String password, final ArrayNode output) {
        ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();

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

    /**
     * Provides register credentials and creates account if one didn't previously
     * exist, or produces an error otherwise
     *
     * @param currentPage instance of current page
     * @param nextPage name of the next page
     * @param credentials credentials provided for creating account
     * @param output ArrayNode where output is passed
     */
    public static void register(final Page currentPage, final String nextPage,
                                final Credentials credentials, final ArrayNode output) {
        ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();

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

    /**
     * Displays movies that begin with a certain sequence
     *
     * @param currentPage instance of current page
     * @param startsWith sequence to be searched
     * @param output ArrayNode where output is passed
     */
    public static void search(final Page currentPage, final String startsWith,
                              final ArrayNode output) {
        ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();

        if (currentPage instanceof MoviesPage) {
            ArrayList<Movie> moviesToSearch = new ArrayList<>(currentPage.getCurrentMoviesList());
            moviesToSearch.removeIf(
                    movie -> !movie.getMovieInfo().getName().startsWith(startsWith));
            currentPage.setCurrentMoviesList(moviesToSearch);

            User currentUser = App.getInstance().getCurrentUser();
            OutputParser.createNonErrorNode(toSend, currentUser, currentPage);
        } else {
            OutputParser.createErrorNode(toSend);
        }

        output.add(toSend);

        currentPage.setCurrentMoviesList(App.getInstance().getCurrentUserMovies());
    }

    /**
     * Filters and / or sorts the list of available movies
     *
     * @param currentPage instance of current page
     * @param rating whether sorting by rating should be increasing or decreasing
     * @param duration whether sorting by duration should be increasing or decreasing
     * @param actors list of actors for filtering
     * @param genres list of genres for filtering
     * @param output ArrayNode where output is passed
     */
    public static void filter(final Page currentPage, final String rating, final String duration,
                              final ArrayList<String> actors, final ArrayList<String> genres,
                              final ArrayNode output) {
        if (currentPage instanceof MoviesPage) {
            currentPage.setCurrentMoviesList(new ArrayList<>(App.getInstance().
                    getCurrentUserMovies()));

            int ratingSort = 0;
            int durationSort = 0;

            if (rating != null) {
                if (rating.equals("increasing")) {
                    ratingSort = 1;
                } else if (rating.equals("decreasing")) {
                    ratingSort = -1;
                }
            }

            if (duration != null) {
                if (duration.equals("increasing")) {
                    durationSort = 1;
                } else if (duration.equals("decreasing")) {
                    durationSort = -1;
                }
            }

            int finalDurationSort = durationSort;
            int finalRatingSort = ratingSort;

            currentPage.getCurrentMoviesList().sort(new Comparator<>() {
                @Override
                public int compare(final Movie o1, final Movie o2) {
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
                            return finalDurationSort * (durationMovie1 - durationMovie2);
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

            if (actors != null) {
                for (String actor : actors) {
                    currentPage.getCurrentMoviesList().removeIf(
                            movie -> !movie.getMovieInfo().getActors().contains(actor));
                }
            }

            if (genres != null) {
                for (String genre : genres) {
                    currentPage.getCurrentMoviesList().removeIf(
                            movie -> !movie.getMovieInfo().getGenres().contains(genre));
                }
            }

            showPage(output);
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    /**
     * Makes according modifications for the "See details" page
     *
     * @param currentPage instance of current page
     * @param movie name of movie to display details for
     * @param output ArrayNode where output is passed
     */
    public static void details(final Page currentPage, final String movie,
                               final ArrayNode output) {
        if (currentPage instanceof DetailsPage) {
            for (Movie movieInstance : App.getInstance().getCurrentUserMovies()) {
                if (movieInstance != null && movieInstance.getMovieInfo().
                        getName().equals(movie)) {
                    currentPage.getCurrentMoviesList().add(movieInstance);
                }
            }

            if (currentPage.getCurrentMoviesList().size() > 0) {
                showPage(output);
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

    /**
     * Converts balance to tokens
     *
     * @param currentPage instance of current page
     * @param quantity number of tokens to be bought
     * @param output ArrayNode where output is passed
     */
    public static void buyTokens(final Page currentPage, final int quantity,
                                 final ArrayNode output) {
        if (currentPage instanceof UpgradesPage) {
            User currentUser = App.getInstance().getCurrentUser();
            int currentBalance = currentUser.getCredentials().getCredentials().getBalance();
            int currentTokensCount = currentUser.getTokensCount();

            if (quantity <= currentBalance) {
                currentUser.setTokensCount(currentTokensCount + quantity);
                currentUser.getCredentials().getCredentials().setBalance(currentBalance - quantity);
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

    /**
     * Converts the account to a premium one
     *
     * @param currentPage instance of current page
     * @param output ArrayNode where output is passed
     */
    public static void buyPremiumAccount(final Page currentPage, final ArrayNode output) {
        if (currentPage instanceof UpgradesPage) {
            User currentUser = App.getInstance().getCurrentUser();
            Credentials currentUserCredentials = currentUser.getCredentials().getCredentials();

            if (currentUserCredentials.getAccountType().equals("standard")
                    && currentUser.getTokensCount() >= MagicNumbers.PREMIUM_ACCOUNT_PRICE) {
                currentUserCredentials.setAccountType("premium");
                currentUser.setTokensCount(
                        currentUser.getTokensCount() - MagicNumbers.PREMIUM_ACCOUNT_PRICE);
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

    /**
     * Purchases a movie if it has not been already purchased
     *
     * @param currentPage instance of current page
     * @param output ArrayNode where output is passed
     */
    public static void purchase(final Page currentPage, final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
                Movie movieToPurchase = currentPage.getCurrentMoviesList().get(0);
                if (!currentUser.getPurchasedMovies().contains(movieToPurchase)) {
                    Credentials currentUserCredentials = currentUser.
                            getCredentials().getCredentials();
                    if (currentUserCredentials.getAccountType().equals("premium")
                            && currentUser.getNumFreePremiumMovies() > 0) {
                        currentUser.setNumFreePremiumMovies(
                                currentUser.getNumFreePremiumMovies() - 1);
                        currentUser.getPurchasedMovies().add(movieToPurchase);
                        showPage(output);
                    } else {
                        if (currentUser.getTokensCount() >= MagicNumbers.MOVIE_PRICE) {
                            currentUser.setTokensCount(
                                    currentUser.getTokensCount() - MagicNumbers.MOVIE_PRICE);

                            currentUser.getPurchasedMovies().add(movieToPurchase);

                            showPage(output);
                        } else {
                            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
                            OutputParser.createErrorNode(toSend);
                            output.add(toSend);
                        }
                    }
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
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    /**
     * Watches a movie if it has been purchased
     *
     * @param currentPage instance of current page
     * @param output ArrayNode where output is passed
     */
    public static void watch(final Page currentPage, final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
                Movie movieToWatch = currentPage.getCurrentMoviesList().get(0);

                if (currentUser.getPurchasedMovies().contains(movieToWatch)
                        && !currentUser.getWatchedMovies().contains(movieToWatch)) {
                    currentUser.getWatchedMovies().add(movieToWatch);

                    showPage(output);
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
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    /**
     * Likes a movie if it has been liked
     *
     * @param currentPage instance of current page
     * @param output ArrayNode where output is passed
     */
    public static void like(final Page currentPage, final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
                Movie movieToLike = currentPage.getCurrentMoviesList().get(0);

                if (currentUser.getWatchedMovies().contains(movieToLike)
                        && !currentUser.getLikedMovies().contains(movieToLike)) {
                    movieToLike.setNumLikes(movieToLike.getNumLikes() + 1);

                    currentUser.getLikedMovies().add(movieToLike);

                    showPage(output);
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
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    /**
     * Rates a movie if it has been watched
     *
     * @param currentPage instance of current page
     * @param rate rating to be given
     * @param output ArrayNode where output is passed
     */
    public static void rate(final Page currentPage, final double rate, final ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
                Movie movieToRate = currentPage.getCurrentMoviesList().get(0);

                if (currentUser.getWatchedMovies().contains(movieToRate)
                        && !currentUser.getRatedMovies().contains(movieToRate)
                        && rate <= MagicNumbers.MAX_RATING) {
                    movieToRate.setSumOfRatings(movieToRate.getSumOfRatings() + rate);
                    movieToRate.setNumRatings(movieToRate.getNumRatings() + 1);
                    movieToRate.setRating(
                            movieToRate.getSumOfRatings() / movieToRate.getNumRatings());

                    currentUser.getRatedMovies().add(movieToRate);

                    showPage(output);
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
        } else {
            ObjectNode toSend = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }
}
