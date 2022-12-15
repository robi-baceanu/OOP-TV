package platform;

import fileio.Input;
import fileio.UserInput;

import java.util.ArrayList;

public final class UsersDatabase implements Database {
    private static UsersDatabase usersDatabase = null;
    private ArrayList<User> users;

    private UsersDatabase() {
        this.users = new ArrayList<>();
    }

    public static UsersDatabase getInstance() {
        if (usersDatabase == null) {
            usersDatabase = new UsersDatabase();
        }
        return usersDatabase;
    }

    public static void deleteInstance() {
        usersDatabase = null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public void initDatabase(final Input inputData) {
        for (UserInput user : inputData.getUsers()) {
            users.add(new User(user));
        }
    }
}
