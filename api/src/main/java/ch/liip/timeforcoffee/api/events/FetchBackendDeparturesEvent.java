package ch.liip.timeforcoffee.api.events;

public class FetchBackendDeparturesEvent {

    private String stationId;

    public FetchBackendDeparturesEvent(String stationId) {
        this.stationId = stationId;
    }

    public String getStationId() {
        return stationId;
    }
}
