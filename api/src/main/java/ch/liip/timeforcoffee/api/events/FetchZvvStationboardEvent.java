package ch.liip.timeforcoffee.api.events;

/**
 * Created by fsantschi on 11/03/15.
 */
public class FetchZvvStationboardEvent {
    private final String stationId;

    public FetchZvvStationboardEvent(String stationId) {
        this.stationId = stationId;
    }

    public String getStationId() {
        return stationId;
    }
}
