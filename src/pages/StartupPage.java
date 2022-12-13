package pages;

import platform.App;

public class StartupPage extends Page {
    public StartupPage() {
        super();
    }

    @Override
    public void changePage(String nextPage) {
        if (nextPage.equals("login") || nextPage.equals("register")) {
            App.getInstance().updateApp(null, nextPage);
        } else {
            App.getInstance().updateApp(null, "logout");
        }
    }
}
