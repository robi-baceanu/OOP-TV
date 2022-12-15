package fileio;

import java.util.ArrayList;

public final class ContainsInput {
    private ArrayList<String> actors;
    private ArrayList<String> genre;

    public ContainsInput() {
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(final ArrayList<String> genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "ContainsInput{"
                + "actors=" + actors
                + ", genre=" + genre
                + '}';
    }
}
