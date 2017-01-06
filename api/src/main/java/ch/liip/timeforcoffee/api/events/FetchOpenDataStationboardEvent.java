package ch.liip.timeforcoffee.api.events;

import java.util.Map;

/**
 * Created by fsantschi on 08/03/15.
 */
public class FetchOpenDataStationboardEvent {
    private Map<String,String> query;

    public FetchOpenDataStationboardEvent(Map<String, String> query) {
        this.query = query;
    }

    public Map<String, String> getQuery() {
        return query;
    }
}
