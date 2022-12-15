package platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;
import fileio.Credentials;
import fileio.UserInput;
import pages.*;

import java.util.ArrayList;
import java.util.Comparator;

public class ActionsParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ActionsParser() {

    }

    public static void showPage(ArrayNode output) {
        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();

        User currentUser = App.getInstance().getCurrentUser();
        Page currentPage = App.getInstance().getCurrentPage();

        if (currentUser != null) {
            OutputParser.createNonErrorNode(toSend, currentUser, currentPage);
        } else {
            OutputParser.createErrorNode(toSend);
        }
        output.add(toSend);
    }

    public static void changePage(String nextPage, ArrayNode output) {
        App.getInstance().getCurrentPage().changePage(nextPage, output);

        if (nextPage.equals("movies")) {
            showPage(output);
        }
    }

    public static void changeToDetailsPage(Page currentPage, String movie, ActionInput action, ArrayNode output) {
        boolean movieToDetailExists = false;
        for (Movie movieInstance : currentPage.getCurrentMoviesList()/*App.getInstance().getCurrentUserMovies()*/) {
            if (movieInstance != null && movieInstance.getMovieInfo().getName().equals(movie)) {
                movieToDetailExists = true;
                break;
            }
        }

        if (movieToDetailExists) {
            ActionsParser.changePage(action.getPage(), output);
//            App.getInstance().getCurrentPage().changePage(nextPage, output);
//
//            Page currentPage = App.getInstance().getCurrentPage();
//
//            if (currentPage instanceof DetailsPage) {
//                for (Movie movieInstance : App.getInstance().getCurrentUserMovies()) {
//                    if (movieInstance != null && movieInstance.getMovieInfo().getName().equals(movie)) {
//                        currentPage.getCurrentMoviesList().add(movieInstance);
//                    }
//                }
//                System.out.println(currentPage.getCurrentMoviesList());
//                if (currentPage.getCurrentMoviesList().size() > 0) {
//                    showPage(output);
//                } else {
//                    ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
//                    OutputParser.createErrorNode(toSend);
//                    output.add(toSend);
//                }
//            } else {
//                ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
//                OutputParser.createErrorNode(toSend);
//                output.add(toSend);
//            }
        }
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
            ArrayList<Movie> moviesToSearch = new ArrayList<>(currentPage.getCurrentMoviesList());
            moviesToSearch.removeIf(movie -> !movie.getMovieInfo().getName().startsWith(startsWith));
            currentPage.setCurrentMoviesList(moviesToSearch);

            User currentUser = App.getInstance().getCurrentUser();
            OutputParser.createNonErrorNode(toSend, currentUser, currentPage);
        } else {
            OutputParser.createErrorNode(toSend);
        }

        output.add(toSend);
    }

    public static void filter(Page currentPage, String rating, String duration, ArrayList<String> actors,
                              ArrayList<String> genres, ArrayNode output) {
        if (currentPage instanceof MoviesPage) {
            currentPage.setCurrentMoviesList(new ArrayList<>(App.getInstance().getCurrentUserMovies()));

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
                    currentPage.getCurrentMoviesList().removeIf(movie -> !movie.getMovieInfo().getActors().contains(actor));
                }
            }

            if (genres != null) {
                for (String genre : genres) {
                    currentPage.getCurrentMoviesList().removeIf(movie -> !movie.getMovieInfo().getGenres().contains(genre));
                }
            }

            showPage(output);
        } else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    public static void details(Page currentPage, String movie, ArrayNode output) {
//        System.out.println(currentPage.getClass());
        if (currentPage instanceof DetailsPage) {
            for (Movie movieInstance : App.getInstance().getCurrentUserMovies()) {
                if (movieInstance != null && movieInstance.getMovieInfo().getName().equals(movie)) {
                    currentPage.getCurrentMoviesList().add(movieInstance);
                }
            }
//            System.out.println(currentPage.getCurrentMoviesList());
            if (currentPage.getCurrentMoviesList().size() > 0) {
                showPage(output);
            } else {
                ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        } else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    public static void buyTokens(Page currentPage, int quantity, ArrayNode output) {
        if (currentPage instanceof UpgradesPage) {
            User currentUser = App.getInstance().getCurrentUser();
            int currentBalance = currentUser.getCredentials().getCredentials().getBalance();
            int currentTokensCount = currentUser.getTokensCount();

            if (quantity <= currentBalance) {
                currentUser.setTokensCount(currentTokensCount + quantity);
                currentUser.getCredentials().getCredentials().setBalance(currentBalance - quantity);
            } else {
                ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        }
        else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    public static void buyPremiumAccount(Page currentPage, ArrayNode output) {
        if (currentPage instanceof UpgradesPage) {
            User currentUser = App.getInstance().getCurrentUser();
            Credentials currentUserCredentials = currentUser.getCredentials().getCredentials();

            if (currentUserCredentials.getAccountType().equals("standard") && currentUser.getTokensCount() >= 10) {
                currentUserCredentials.setAccountType("premium");
                currentUser.setTokensCount(currentUser.getTokensCount() - 10);
            } else {
                ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        } else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    public static void purchase(Page currentPage, String movie, ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
//                if (currentPage.getCurrentMoviesList().get(0).getMovieInfo().getName().equals(movie)) {
                    Movie movieToPurchase = currentPage.getCurrentMoviesList().get(0);
                    if (!currentUser.getPurchasedMovies().contains(movieToPurchase)) {
                        Credentials currentUserCredentials = currentUser.getCredentials().getCredentials();
                        if (currentUserCredentials.getAccountType().equals("premium")) {
                            if (currentUser.getNumFreePremiumMovies() > 0) {
                                currentUser.setNumFreePremiumMovies(currentUser.getNumFreePremiumMovies() - 1);
                                currentUser.getPurchasedMovies().add(movieToPurchase);
                                showPage(output);
                            } else {
                                if (currentUser.getTokensCount() >= 2) {
                                    currentUser.setTokensCount(currentUser.getTokensCount() - 2);

                                    currentUser.getPurchasedMovies().add(movieToPurchase);

                                    showPage(output);
                                } else {
                                    ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                                    OutputParser.createErrorNode(toSend);
                                    output.add(toSend);
                                }
                            }
                        } else {
                            if (currentUser.getTokensCount() >= 2) {
                                currentUser.setTokensCount(currentUser.getTokensCount() - 2);

                                currentUser.getPurchasedMovies().add(movieToPurchase);

                                showPage(output);
                            } else {
                                ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                                OutputParser.createErrorNode(toSend);
                                output.add(toSend);
                            }
                        }
                    } else {
                        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                        OutputParser.createErrorNode(toSend);
                        output.add(toSend);
                    }
//                } else {
//                    ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
//                    OutputParser.createErrorNode(toSend);
//                    output.add(toSend);
//                }
            } else {
                ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        } else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    public static void watch(Page currentPage, String movie, ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
//                if (currentPage.getCurrentMoviesList().get(0).getMovieInfo().getName().equals(movie)) {
                    Movie movieToWatch = currentPage.getCurrentMoviesList().get(0);

                    if (currentUser.getPurchasedMovies().contains(movieToWatch) &&
                        !currentUser.getWatchedMovies().contains(movieToWatch)) {
                        currentUser.getWatchedMovies().add(movieToWatch);

                        showPage(output);
                    } else {
                        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                        OutputParser.createErrorNode(toSend);
                        output.add(toSend);
                    }
//                } else {
//                    ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
//                    OutputParser.createErrorNode(toSend);
//                    output.add(toSend);
//                }
            } else {
                ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        } else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    public static void like(Page currentPage, String movie, ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
//                if (currentPage.getCurrentMoviesList().get(0).getMovieInfo().getName().equals(movie)) {
                    Movie movieToLike = currentPage.getCurrentMoviesList().get(0);

                    if (currentUser.getWatchedMovies().contains(movieToLike) &&
                        !currentUser.getLikedMovies().contains(movieToLike)) {
                        movieToLike.setNumLikes(movieToLike.getNumLikes() + 1);

                        currentUser.getLikedMovies().add(movieToLike);

                        showPage(output);
                    } else {
                        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                        OutputParser.createErrorNode(toSend);
                        output.add(toSend);
                    }
//                } else {
//                    ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
//                    OutputParser.createErrorNode(toSend);
//                    output.add(toSend);
//                }
            } else {
                ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        } else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }

    public static void rate(Page currentPage, String movie, double rate, ArrayNode output) {
        User currentUser = App.getInstance().getCurrentUser();

        if (currentPage instanceof DetailsPage) {
            if (currentPage.getCurrentMoviesList().size() > 0) {
//                if (currentPage.getCurrentMoviesList().get(0).getMovieInfo().getName().equals(movie)) {
                    Movie movieToRate = currentPage.getCurrentMoviesList().get(0);

                    if (currentUser.getWatchedMovies().contains(movieToRate) &&
                        !currentUser.getRatedMovies().contains(movieToRate) &&
                        rate <= 5) {
                        movieToRate.setSumOfRatings(movieToRate.getSumOfRatings() + rate);
                        movieToRate.setNumRatings(movieToRate.getNumRatings() + 1);
                        movieToRate.setRating(movieToRate.getSumOfRatings() / movieToRate.getNumRatings());

                        currentUser.getRatedMovies().add(movieToRate);

                        showPage(output);
                    } else {
                        ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                        OutputParser.createErrorNode(toSend);
                        output.add(toSend);
                    }
//                } else {
//                    ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
//                    OutputParser.createErrorNode(toSend);
//                    output.add(toSend);
//                }
            } else {
                ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
                OutputParser.createErrorNode(toSend);
                output.add(toSend);
            }
        } else {
            ObjectNode toSend = OBJECT_MAPPER.createObjectNode();
            OutputParser.createErrorNode(toSend);
            output.add(toSend);
        }
    }
}
