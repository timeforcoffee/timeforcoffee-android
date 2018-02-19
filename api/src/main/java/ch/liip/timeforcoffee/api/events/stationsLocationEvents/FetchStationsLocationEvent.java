package ch.liip.timeforcoffee.api.events.stationsLocationEvents;

import java.util.Map;

public class FetchStationsLocationEvent {

    private Map<String,String> query;

    public FetchStationsLocationEvent(Map<String,String> query) {
        this.query = query;
    }

    public Map<String,String> getQuery() {
        return query;
    }
}
