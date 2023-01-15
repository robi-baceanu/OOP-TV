package platform;

import fileio.MovieInput;
import fileio.UserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class that describes a user
 *
 * @author wh1ter0se
 */
public final class User {
    private UserInput credentials;
    private int tokensCount;
    private int numFreePremiumMovies;
    private ArrayList<Movie> purchasedMovies;
    private ArrayList<Movie> watchedMovies;
    private ArrayList<Movie> likedMovies;
    private ArrayList<Movie> ratedMovies;
    private ArrayList<Notification> notifications;
    private LinkedList<String> accessedPages;
    private ArrayList<String> subscriptions;
    private HashMap<String, Double> ratingsGiven;

    public User(final UserInput credentials) {
        this.credentials = credentials;
        this.tokensCount = 0;
        this.numFreePremiumMovies = MagicNumbers.FREE_PREMIUM_MOVIES_NUMBER;
        this.purchasedMovies = new ArrayList<>();
        this.watchedMovies = new ArrayList<>();
        this.likedMovies = new ArrayList<>();
        this.ratedMovies = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.ratingsGiven = new HashMap<>();
    }

    public UserInput getCredentials() {
        return credentials;
    }

    public void setCredentials(final UserInput credentials) {
        this.credentials = credentials;
    }

    public int getTokensCount() {
        return tokensCount;
    }

    public void setTokensCount(final int tokensCount) {
        this.tokensCount = tokensCount;
    }

    public int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public ArrayList<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    public void setPurchasedMovies(final ArrayList<Movie> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public ArrayList<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(final ArrayList<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public ArrayList<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(final ArrayList<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(final ArrayList<Movie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public LinkedList<String> getAccessedPages() {
        return accessedPages;
    }

    public void setAccessedPages(LinkedList<String> accessedPages) {
        this.accessedPages = accessedPages;
    }

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(ArrayList<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public HashMap<String, Double> getRatingsGiven() {
        return ratingsGiven;
    }

    public void setRatingsGiven(HashMap<String, Double> ratingsGiven) {
        this.ratingsGiven = ratingsGiven;
    }

    /**
     * Method that updates a user's notifications
     *
     * @param eventType whether a movie was added or deleted
     * @param movie movie that was added / deleted
     */
    public void update(String eventType, MovieInput movie) {
        if (eventType.equals("add")) {
            for (String genre : movie.getGenres()) {
                if (this.getSubscriptions().contains(genre) &&
                        !movie.getCountriesBanned().contains(this.getCredentials().getCredentials().getCountry())) {
                    this.getNotifications().add(new Notification(movie.getName(), "ADD"));
                    break;
                }
            }
        } else if (eventType.equals("delete")) {
            for (Movie purchasedMovie : this.getPurchasedMovies()) {
                if (purchasedMovie.getMovieInfo().getName().equals(movie.getName())) {
                    this.getNotifications().add(new Notification(movie.getName(), "DELETE"));

                    if (this.getCredentials().getCredentials().getAccountType().equals("premium")) {
                        this.setNumFreePremiumMovies(this.getNumFreePremiumMovies() + 1);
                    } else if (this.getCredentials().getCredentials().getAccountType().equals("standard")) {
                        this.setTokensCount(this.getTokensCount() + MagicNumbers.MOVIE_PRICE);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "User{"
                + "credentials=" + credentials
                + ", tokensCount=" + tokensCount
                + ", numFreePremiumMovies=" + numFreePremiumMovies
                + ", purchasedMovies=" + purchasedMovies
                + ", watchedMovies=" + watchedMovies
                + ", likedMovies=" + likedMovies
                + ", ratedMovies=" + ratedMovies
                + '}';
    }
}
