package ch.liip.timeforcoffee.api.events;

import ch.liip.timeforcoffee.api.Station;

/**
 * Created by fsantschi on 11/03/15.
 */
public class FetchDeparturesEvent {

    private final Station station;

    public FetchDeparturesEvent(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
