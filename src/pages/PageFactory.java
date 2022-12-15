package pages;

/**
 * Utility class for generating instances of pages
 * (implemented following Factory pattern)
 *
 * @author wh1ter0se
 */
public final class PageFactory {
    private PageFactory() {

    }

    /**
     * Method that generates page instances
     *
     * @param pageType type of page desired to be generated
     * @return instance of generated page
     */
    public static Page getPage(final String pageType) {
        return switch (pageType) {
            case "logout" -> new StartupPage();
            case "login" -> new LoginPage();
            case "register" -> new RegisterPage();
            case "homepage" -> new HomePage();
            case "movies" -> new MoviesPage();
            case "see details" -> new DetailsPage();
            case "upgrades" -> new UpgradesPage();
            default -> null;
        };
    }
}
