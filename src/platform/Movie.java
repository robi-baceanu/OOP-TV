package platform;

import fileio.MovieInput;

/**
 * Class that describes a movie
 *
 * @author wh1ter0se
 */
public final class Movie {
    private final MovieInput movieInfo;
    private int numLikes;
    private double rating;
    private int numRatings;
    private double sumOfRatings;

    public Movie(final MovieInput movieInfo) {
        this.movieInfo = movieInfo;
        this.numLikes = 0;
        this.rating = 0;
        this.numRatings = 0;
        this.sumOfRatings = 0;
    }

    public MovieInput getMovieInfo() {
        return movieInfo;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(final int numLikes) {
        this.numLikes = numLikes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(final int numRatings) {
        this.numRatings = numRatings;
    }

    public double getSumOfRatings() {
        return sumOfRatings;
    }

    public void setSumOfRatings(final double sumOfRatings) {
        this.sumOfRatings = sumOfRatings;
    }

    @Override
    public String toString() {
        return "Movie{"
                + "movieInfo=" + movieInfo
                + ", numLikes=" + numLikes
                + ", rating=" + rating
                + ", numRatings=" + numRatings
                + '}';
    }
}
