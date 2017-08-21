package ch.liip.timeforcoffee.api.events;

import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;

public class FetchConnectionsEvent {

    private final Station station;
    private final Departure departure;

    public FetchConnectionsEvent(Station station, Departure departure) {
        this.station = station;
        this.departure = departure;
    }

    public Station getStation() {
        return station;
    }

    public Departure getDeparture() {
        return departure;
    }
}
