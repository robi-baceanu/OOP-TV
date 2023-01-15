package platform;

import fileio.Input;
import fileio.UserInput;

import java.util.ArrayList;

/**
 * Class that stores all users of the platform
 *
 * @author wh1ter0se
 */
public final class UsersDatabase implements Database {
    private static UsersDatabase usersDatabase = null;
    private ArrayList<User> users;

    private UsersDatabase() {
        this.users = new ArrayList<>();
    }

    /**
     * Method for returning instance of the users' database
     * (implemented following Singleton pattern)
     *
     * @return instance of users' database
     */
    public static UsersDatabase getInstance() {
        if (usersDatabase == null) {
            usersDatabase = new UsersDatabase();
        }
        return usersDatabase;
    }

    /**
     * Deletes instance of the application,
     * setting its UsersDatabase field to null
     */
    public static void deleteInstance() {
        usersDatabase = null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initDatabase(final Input inputData) {
        for (UserInput user : inputData.getUsers()) {
            users.add(new User(user));
        }
    }
}
