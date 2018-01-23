package ch.liip.timeforcoffee.api.events.stationsSearchEvents;

/**
 * Created by fsantschi on 11/03/15.
 */
public class FetchZvvStationsSearchEvent {
    private final String searchQuery;

    public FetchZvvStationsSearchEvent(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
