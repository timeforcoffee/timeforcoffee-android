package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.api.Departure;

/**
 * Created by fsantschi on 13/03/15.
 */
public class DeparturesFetchedEvent {
    private final List<Departure> departures;

    public DeparturesFetchedEvent(List<Departure> departures) {
        this.departures = departures;
    }

    public List<Departure> getDepartures() {
        return departures;
    }
}
