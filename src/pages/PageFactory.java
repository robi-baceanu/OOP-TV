package pages;

public final class PageFactory {
    private PageFactory() {

    }

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
