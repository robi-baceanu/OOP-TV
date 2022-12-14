package platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Credentials;
import pages.Page;

import java.util.ArrayList;

public class OutputParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private OutputParser() {

    }

    public static void createErrorNode(ObjectNode toSend) {
        toSend.put("error", "Error");
        ArrayNode currentMoviesNode = OBJECT_MAPPER.createArrayNode();
        toSend.set("currentMoviesList", currentMoviesNode);
        toSend.set("currentUser", null);
    }

    public static void createMovieNode(ObjectNode movieNode, Movie movie) {
        if (movie != null) {
            movieNode.put("name", movie.getMovieInfo().getName());
            movieNode.put("year", movie.getMovieInfo().getYear());
            movieNode.put("duration", movie.getMovieInfo().getDuration());
            ArrayNode genresNode = OBJECT_MAPPER.createArrayNode();
            for (String genre : movie.getMovieInfo().getGenres()) {
                genresNode.add(genre);
            }
            movieNode.set("genres", genresNode);
            ArrayNode actorsNode = OBJECT_MAPPER.createArrayNode();
            for (String actor : movie.getMovieInfo().getActors()) {
                actorsNode.add(actor);
            }
            movieNode.set("actors", actorsNode);
            ArrayNode countriesNode = OBJECT_MAPPER.createArrayNode();
            for (String country : movie.getMovieInfo().getCountriesBanned()) {
                countriesNode.add(country);
            }
            movieNode.set("countriesBanned", countriesNode);
            movieNode.put("numLikes", movie.getNumLikes());
            movieNode.put("rating", movie.getRating());
            movieNode.put("numRatings", movie.getNumRatings());
        }
    }

    public static void createMoviesArrayNode(ArrayNode moviesNode, ArrayList<Movie> moviesList) {
        for (Movie movie : moviesList) {
            ObjectNode movieNode = OBJECT_MAPPER.createObjectNode();
            OutputParser.createMovieNode(movieNode, movie);
            moviesNode.add(movieNode);
        }
    }

    public static void createCurrentUserNode(User currentUser, ObjectNode currentUserNode) {
        if (currentUser != null) {
            Credentials currentUserCredentials = currentUser.getCredentials().getCredentials();

            ObjectNode credentialsNode = OBJECT_MAPPER.createObjectNode();
            credentialsNode.put("name", currentUserCredentials.getName());
            credentialsNode.put("password", currentUserCredentials.getPassword());
            credentialsNode.put("accountType", currentUserCredentials.getAccountType());
            credentialsNode.put("country", currentUserCredentials.getCountry());
            credentialsNode.put("balance", ((Integer) currentUserCredentials.getBalance()).toString());
            currentUserNode.set("credentials", credentialsNode);

            currentUserNode.put("tokensCount", currentUser.getTokensCount());
            currentUserNode.put("numFreePremiumMovies", currentUser.getNumFreePremiumMovies());

            ArrayNode purchasedMoviesNode = OBJECT_MAPPER.createArrayNode();
            createMoviesArrayNode(purchasedMoviesNode, currentUser.getPurchasedMovies());
            currentUserNode.set("purchasedMovies", purchasedMoviesNode);

            ArrayNode watchedMoviesNode = OBJECT_MAPPER.createArrayNode();
            createMoviesArrayNode(watchedMoviesNode, currentUser.getWatchedMovies());
            currentUserNode.set("watchedMovies", watchedMoviesNode);

            ArrayNode likedMoviesNode = OBJECT_MAPPER.createArrayNode();
            createMoviesArrayNode(likedMoviesNode, currentUser.getLikedMovies());
            currentUserNode.set("likedMovies", likedMoviesNode);

            ArrayNode ratedMoviesNode = OBJECT_MAPPER.createArrayNode();
            createMoviesArrayNode(ratedMoviesNode, currentUser.getRatedMovies());
            currentUserNode.set("ratedMovies", ratedMoviesNode);
        }
    }

    public static void createNonErrorNode(ObjectNode toSend, User currentUser, Page currentPage) {
        toSend.set("error", null);
        ArrayNode currentMoviesNode = OBJECT_MAPPER.createArrayNode();
        OutputParser.createMoviesArrayNode(currentMoviesNode, currentPage.getCurrentMoviesList());
        toSend.set("currentMoviesList", currentMoviesNode);
        ObjectNode currentUserNode = OBJECT_MAPPER.createObjectNode();
        OutputParser.createCurrentUserNode(currentUser, currentUserNode);
        toSend.set("currentUser", currentUserNode);
    }
}
