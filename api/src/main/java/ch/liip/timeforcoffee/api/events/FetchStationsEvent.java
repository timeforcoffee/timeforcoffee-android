package ch.liip.timeforcoffee.api.events;

/**
 * Created by fsantschi on 11/03/15.
 */
public class FetchStationsEvent {
    private String searchQuery;

    public FetchStationsEvent(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
