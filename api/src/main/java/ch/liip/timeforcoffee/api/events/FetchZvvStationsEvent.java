package ch.liip.timeforcoffee.api.events;

/**
 * Created by fsantschi on 11/03/15.
 */
public class FetchZvvStationsEvent {
    private final String searchQuery;

    public FetchZvvStationsEvent(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
