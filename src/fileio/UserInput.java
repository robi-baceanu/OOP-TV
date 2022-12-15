package fileio;

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
