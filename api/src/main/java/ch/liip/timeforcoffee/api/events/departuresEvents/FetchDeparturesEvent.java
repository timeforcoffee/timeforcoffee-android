package ch.liip.timeforcoffee.api.events.departuresEvents;

public class FetchDeparturesEvent {

    private final String stationId;

    public FetchDeparturesEvent(String stationId) {
        this.stationId = stationId;
    }

    public String getStationId() {
        return stationId;
    }
}
