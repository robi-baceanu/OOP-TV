package platform;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Credentials;
import pages.Page;

import java.util.ArrayList;

/**
 * Utility class for generating output
 * ObjectNodes and ArrayNodes
 *
 * @author wh1ter0se
 */
public final class OutputParser {
    private OutputParser() {

    }

    /**
     * Generates an ObjectNode that describes an error
     *
     * @param toSend ObjectNode to parse
     */
    public static void createErrorNode(final ObjectNode toSend) {
        toSend.put("error", "Error");
        ArrayNode currentMoviesNode = MagicNumbers.OBJECT_MAPPER.createArrayNode();
        toSend.set("currentMoviesList", currentMoviesNode);
        toSend.set("currentUser", null);
    }

    /**
     * Generates an ObjectNode that describes a movie
     *
     * @param movieNode ObjectNode to parse
     * @param movie instance of movie desired for output
     */
    public static void createMovieNode(final ObjectNode movieNode, final Movie movie) {
        if (movie != null) {
            movieNode.put("name", movie.getMovieInfo().getName());
            movieNode.put("year", movie.getMovieInfo().getYear());
            movieNode.put("duration", movie.getMovieInfo().getDuration());
            ArrayNode genresNode = MagicNumbers.OBJECT_MAPPER.createArrayNode();
            for (String genre : movie.getMovieInfo().getGenres()) {
                genresNode.add(genre);
            }
            movieNode.set("genres", genresNode);
            ArrayNode actorsNode = MagicNumbers.OBJECT_MAPPER.createArrayNode();
            for (String actor : movie.getMovieInfo().getActors()) {
                actorsNode.add(actor);
            }
            movieNode.set("actors", actorsNode);
            ArrayNode countriesNode = MagicNumbers.OBJECT_MAPPER.createArrayNode();
            for (String country : movie.getMovieInfo().getCountriesBanned()) {
                countriesNode.add(country);
            }
            movieNode.set("countriesBanned", countriesNode);
            movieNode.put("numLikes", movie.getNumLikes());
            movieNode.put("rating", movie.getRating());
            movieNode.put("numRatings", movie.getNumRatings());
        }
    }

    /**
     * Generates an ArrayNode that describes a list of movies
     *
     * @param moviesNode ArrayNode to parse
     * @param moviesList list of movies desired for output
     */
    public static void createMoviesArrayNode(final ArrayNode moviesNode,
                                             final ArrayList<Movie> moviesList) {
        for (Movie movie : moviesList) {
            ObjectNode movieNode = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            OutputParser.createMovieNode(movieNode, movie);
            moviesNode.add(movieNode);
        }
    }

    /**
     * Generates an ObjectNode that describes a user
     *
     * @param currentUser user desired for output
     * @param currentUserNode ObjectNode to parse
     */
    public static void createCurrentUserNode(final User currentUser,
                                             final ObjectNode currentUserNode) {
        if (currentUser != null) {
            Credentials currentUserCredentials = currentUser.getCredentials().getCredentials();

            ObjectNode credentialsNode = MagicNumbers.OBJECT_MAPPER.createObjectNode();
            credentialsNode.put("name", currentUserCredentials.getName());
            credentialsNode.put("password", currentUserCredentials.getPassword());
            credentialsNode.put("accountType", currentUserCredentials.getAccountType());
            credentialsNode.put("country", currentUserCredentials.getCountry());
            credentialsNode.put("balance",
                    ((Integer) currentUserCredentials.getBalance()).toString());
            currentUserNode.set("credentials", credentialsNode);

            currentUserNode.put("tokensCount", currentUser.getTokensCount());
            currentUserNode.put("numFreePremiumMovies", currentUser.getNumFreePremiumMovies());

            ArrayNode purchasedMoviesNode = MagicNumbers.OBJECT_MAPPER.createArrayNode();
            createMoviesArrayNode(purchasedMoviesNode, currentUser.getPurchasedMovies());
            currentUserNode.set("purchasedMovies", purchasedMoviesNode);

            ArrayNode watchedMoviesNode = MagicNumbers.OBJECT_MAPPER.createArrayNode();
            createMoviesArrayNode(watchedMoviesNode, currentUser.getWatchedMovies());
            currentUserNode.set("watchedMovies", watchedMoviesNode);

            ArrayNode likedMoviesNode = MagicNumbers.OBJECT_MAPPER.createArrayNode();
            createMoviesArrayNode(likedMoviesNode, currentUser.getLikedMovies());
            currentUserNode.set("likedMovies", likedMoviesNode);

            ArrayNode ratedMoviesNode = MagicNumbers.OBJECT_MAPPER.createArrayNode();
            createMoviesArrayNode(ratedMoviesNode, currentUser.getRatedMovies());
            currentUserNode.set("ratedMovies", ratedMoviesNode);
        }
    }

    /**
     * Generates a complete output ObjectNode, describing a user, a list
     * of movies and signaling that no errors were produced
     *
     * @param toSend ObjectNode to parse
     * @param currentUser user desired for output
     * @param currentPage page that provides list of movies to be displayed
     */
    public static void createNonErrorNode(final ObjectNode toSend, final User currentUser,
                                          final Page currentPage) {
        toSend.set("error", null);
        ArrayNode currentMoviesNode = MagicNumbers.OBJECT_MAPPER.createArrayNode();
        OutputParser.createMoviesArrayNode(currentMoviesNode, currentPage.getCurrentMoviesList());
        toSend.set("currentMoviesList", currentMoviesNode);
        ObjectNode currentUserNode = MagicNumbers.OBJECT_MAPPER.createObjectNode();
        OutputParser.createCurrentUserNode(currentUser, currentUserNode);
        toSend.set("currentUser", currentUserNode);
    }
}
