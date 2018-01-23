package ch.liip.timeforcoffee.api.events.stationsSearchOneEvents;

public class FetchStationsSearchOneEvent {

    private String searchQuery;

    public FetchStationsSearchOneEvent(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
