package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.zvv.Departure;

/**
 * Created by fsantschi on 11/03/15.
 */
public class ZvvStationboardFetchedEvent {
    private final List<Departure> departures;

    public ZvvStationboardFetchedEvent(List<Departure> departures) {
        this.departures = departures;
    }

    public List<Departure> getDepartures() {
        return departures;
    }
}
