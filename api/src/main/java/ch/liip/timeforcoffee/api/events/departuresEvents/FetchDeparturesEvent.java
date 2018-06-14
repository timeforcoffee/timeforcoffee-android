package ch.liip.timeforcoffee.api.events.departuresEvents;

public class FetchDeparturesEvent {

    private final int stationId;

    public FetchDeparturesEvent(int stationId) {
        this.stationId = stationId;
    }

    public int getStationId() {
        return stationId;
    }
}
