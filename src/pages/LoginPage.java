package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.UserInput;
import platform.App;
import platform.User;
import platform.UsersDatabase;

public class LoginPage extends Page {
    private String name;
    private String password;

    public LoginPage() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User tryLogin() {
        for (User user : UsersDatabase.getInstance().getUsers()) {
            UserInput userCredentials = user.getCredentials();
            if (userCredentials.getCredentials().getName().equals(this.name) &&
                userCredentials.getCredentials().getPassword().equals(this.password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void changePage(String nextPage, ArrayNode output) {
        /* nextPage is ignored, because we know the page we need to move to,
        given by the successful/unsuccessful login */
        User userLoggingIn = tryLogin();

        if (userLoggingIn != null) {
            App.getInstance().updateApp(userLoggingIn, "homepage");
        } else {
            App.getInstance().updateApp(null, "logout");
        }
    }
}
