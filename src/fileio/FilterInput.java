package fileio;

public class FilterInput {
    private SortInput sort;
    private ContainsInput contains;

    public FilterInput() {
    }

    public SortInput getSort() {
        return sort;
    }

    public void setSort(SortInput sort) {
        this.sort = sort;
    }

    public ContainsInput getContains() {
        return contains;
    }

    public void setContains(ContainsInput contains) {
        this.contains = contains;
    }

    @Override
    public String toString() {
        return "FilterInput{" +
                "sort=" + sort +
                ", contains=" + contains +
                '}';
    }
}
