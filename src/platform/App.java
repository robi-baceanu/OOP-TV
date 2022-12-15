package platform;

import pages.Page;
import pages.PageFactory;

import java.util.ArrayList;

/**
 * Class that describes the state of the app, described by the current user,
 * a list of movies that can be accessed at some point by the user and the
 * current page
 *
 * @author wh1ter0se
 */
public final class App {
    private static App app = null;
    private User currentUser;
    private ArrayList<Movie> currentUserMovies;
    private Page currentPage;

    private App() {

    }

    /**
     * Method for returning instance of the application
     * (implemented following Singleton pattern)
     *
     * @return instance of App
     */
    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    /**
     * Deletes instance of the application,
     * setting its App field to null
     */
    public static void deleteInstance() {
        app = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<Movie> getCurrentUserMovies() {
        return currentUserMovies;
    }

    public void setCurrentUserMovies(final ArrayList<Movie> currentUserMovies) {
        this.currentUserMovies = currentUserMovies;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(final Page currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Initializes the application, by setting the current user to null, the current
     * page to the StartUp one, and the list of movies accessible by the current user
     * to an empty list
     */
    public void initApp() {
        currentUser = null;
        currentPage = PageFactory.getPage("logout");
        currentUserMovies = new ArrayList<>();
    }

    /**
     * Updates the application's fields to the new ones
     *
     * @param user new current user
     * @param nextPage next page that is to be accessed
     */
    public void updateApp(final User user, final String nextPage) {
        app.setCurrentPage(PageFactory.getPage(nextPage));

        app.setCurrentUser(user);

        if (user != null) {
            app.setCurrentUserMovies(new ArrayList<>(MoviesDatabase.getInstance().getMovies()));
            String userCountry = user.getCredentials().getCredentials().getCountry();
            app.getCurrentUserMovies().removeIf(
                    movie -> movie.getMovieInfo().getCountriesBanned().contains(userCountry));
        } else {
            app.setCurrentUserMovies(new ArrayList<>());
        }
    }
}
