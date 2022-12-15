package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.UserInput;
import platform.App;
import platform.User;
import platform.UsersDatabase;

public final class RegisterPage extends Page {
    private UserInput credentials;

    public RegisterPage() {
        super();
    }

    public UserInput getCredentials() {
        return credentials;
    }

    public void setCredentials(final UserInput credentials) {
        this.credentials = credentials;
    }

    public User tryRegister() {
        for (User user : UsersDatabase.getInstance().getUsers()) {
            UserInput userCredentials = user.getCredentials();
            if (userCredentials.getCredentials().getName().equals(
                    credentials.getCredentials().getName())
                    && userCredentials.getCredentials().getPassword().equals(
                    credentials.getCredentials().getPassword())) {
                return null;
            }
        }
        return new User(credentials);
    }

    @Override
    public void changePage(final String nextPage, final ArrayNode output) {
        /* nextPage is ignored, because we know the page we need to move to,
        given by the successful/unsuccessful register */
        User userCreatingAccount = tryRegister();

        if (userCreatingAccount != null) {
            UsersDatabase.getInstance().getUsers().add(userCreatingAccount);
            App.getInstance().updateApp(userCreatingAccount, "homepage");
        } else {
            App.getInstance().updateApp(null, "logout");
        }
    }
}
