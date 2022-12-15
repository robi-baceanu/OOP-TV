package platform;

import fileio.UserInput;

import java.util.ArrayList;

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

    public User(final UserInput credentials) {
        this.credentials = credentials;
        this.tokensCount = 0;
        this.numFreePremiumMovies = MagicNumbers.FREE_PREMIUM_MOVIES_NUMBER;
        this.purchasedMovies = new ArrayList<>();
        this.watchedMovies = new ArrayList<>();
        this.likedMovies = new ArrayList<>();
        this.ratedMovies = new ArrayList<>();
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
