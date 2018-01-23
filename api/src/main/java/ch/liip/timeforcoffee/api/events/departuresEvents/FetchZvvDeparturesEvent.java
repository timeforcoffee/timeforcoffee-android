package ch.liip.timeforcoffee.api.events.departuresEvents;

public class FetchZvvDeparturesEvent {

    private final String stationId;

    public FetchZvvDeparturesEvent(String stationId) {
        this.stationId = stationId;
    }

    public String getStationId() {
        return stationId;
    }
}
