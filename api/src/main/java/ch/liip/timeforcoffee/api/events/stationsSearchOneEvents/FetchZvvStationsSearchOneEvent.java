package ch.liip.timeforcoffee.api.events.stationsSearchOneEvents;

public class FetchZvvStationsSearchOneEvent {

    private final String searchQuery;

    public FetchZvvStationsSearchOneEvent(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
