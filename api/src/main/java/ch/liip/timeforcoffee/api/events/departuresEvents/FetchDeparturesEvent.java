package ch.liip.timeforcoffee.api.events.departuresEvents;

/**
 * Created by fsantschi on 11/03/15.
 */
public class FetchDeparturesEvent {

    private final String stationId;

    public FetchDeparturesEvent(String stationId) {
        this.stationId = stationId;
    }

    public String getStationId() {
        return stationId;
    }
}
