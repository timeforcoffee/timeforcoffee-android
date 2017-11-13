package ch.liip.timeforcoffee.api.events;

public class FetchZvvStationboardEvent {

    private final String stationId;

    public FetchZvvStationboardEvent(String stationId) {
        this.stationId = stationId;
    }

    public String getStationId() {
        return stationId;
    }
}
