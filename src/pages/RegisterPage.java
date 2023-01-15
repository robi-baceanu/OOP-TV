package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.UserInput;
import platform.App;
import platform.User;
import platform.UsersDatabase;

import java.util.LinkedList;

/**
 * Page where user can register
 *
 * @author wh1ter0se
 */
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

    /**
     * Method that attempts registering a new user
     *
     * @return instance of the newly registered user, or null if registration fails
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePage(final String nextPage, final ArrayNode output) {
        /* nextPage is ignored, because we know the page we need to move to,
        given by the successful/unsuccessful register */
        User userCreatingAccount = tryRegister();

        if (userCreatingAccount != null) {
            UsersDatabase.getInstance().getUsers().add(userCreatingAccount);
            userCreatingAccount.setAccessedPages(new LinkedList<>());
            userCreatingAccount.getAccessedPages().addLast("homepage");
            App.getInstance().updateApp(userCreatingAccount, "homepage");
        } else {
            App.getInstance().updateApp(null, "logout");
        }
    }
}
