package platform;

import fileio.MovieInput;

public class Movie {
    private MovieInput movieInfo;
    private int numLikes;
    private double rating;
    private int numRatings;

    public Movie(MovieInput movieInfo) {
        this.movieInfo = movieInfo;
        this.numLikes = 0;
        this.rating = 0;
        this.numRatings = 0;
    }

    public MovieInput getMovieInfo() {
        return movieInfo;
    }

    public void setMovieInfo(MovieInput movieInfo) {
        this.movieInfo = movieInfo;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieInfo=" + movieInfo +
                ", numLikes=" + numLikes +
                ", rating=" + rating +
                ", numRatings=" + numRatings +
                '}';
    }
}
