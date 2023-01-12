package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.MoviesPage;
import pages.Page;
import platform.App;
import platform.MagicNumbers;
import platform.Movie;
import platform.OutputParser;

import java.util.ArrayList;
import java.util.Comparator;

import static platform.ActionsParser.showPage;

/**
 * @author wh1ter0se
 */
public final class FilterCommand implements Command {
    private final String rating;
    private final String duration;
    private final ArrayList<String> actors;
    private final ArrayList<String> genres;

    /**
     * Filters and / or sorts the list of available movies
     *
     * @param rating whether sorting by rating should be increasing or decreasing
     * @param duration whether sorting by duration should be increasing or decreasing
     * @param actors list of actors for filtering
     * @param genres list of genres for filtering
     */
    public FilterCommand(final String rating, final String duration,
                         final ArrayList<String> actors, final ArrayList<String> genres) {
        this.rating = rating;
        this.duration = duration;
        this.actors = actors;
        this.genres = genres;
    }

    @Override
    public void execute(final Page currentPage, final ArrayNode output) {
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
}
