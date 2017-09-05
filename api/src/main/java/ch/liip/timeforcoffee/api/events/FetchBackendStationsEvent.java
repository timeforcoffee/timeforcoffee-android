package ch.liip.timeforcoffee.api.events;

public class FetchBackendStationsEvent {

    private String query;

    public FetchBackendStationsEvent(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
