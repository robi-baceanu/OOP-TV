package platform;

import pages.Page;
import pages.PageFactory;

import java.util.ArrayList;

public class App {
    private static App app = null;
    private User currentUser;
    private ArrayList<Movie> currentUserMovies;
    private Page currentPage;

    private App() {

    }

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    public static void deleteInstance() {
        app = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<Movie> getCurrentUserMovies() {
        return currentUserMovies;
    }

    public void setCurrentUserMovies(ArrayList<Movie> currentUserMovies) {
        this.currentUserMovies = currentUserMovies;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    public void initApp() {
        currentUser = null;
        currentPage = PageFactory.getPage("logout");
        currentUserMovies = new ArrayList<Movie>();
    }

    public void updateApp(User user, String nextPage) {
        app.setCurrentPage(PageFactory.getPage(nextPage));

        app.setCurrentUser(user);

        if (user != null) {
            app.setCurrentUserMovies(MoviesDatabase.getInstance().getMovies());
            String userCountry = user.getCredentials().getCredentials().getCountry();
            app.getCurrentUserMovies().
                    removeIf(movie -> movie.getMovieInfo().getCountriesBanned().contains(userCountry));
        } else {
            app.setCurrentUserMovies(new ArrayList<>());
        }

    }
}
