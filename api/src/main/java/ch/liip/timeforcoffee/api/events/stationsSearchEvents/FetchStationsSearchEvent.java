package ch.liip.timeforcoffee.api.events.stationsSearchEvents;

public class FetchStationsSearchEvent {

    private String searchQuery;

    public FetchStationsSearchEvent(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
