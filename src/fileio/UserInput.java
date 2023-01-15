package fileio;

/**
 * FileIO class that describes a user and its fields
 *
 * @author wh1ter0se
 */
public final class UserInput {
    private Credentials credentials;

    public UserInput() {
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return "UserInput{"
                + "credentials=" + credentials
                + '}';
    }
}
