package ch.liip.timeforcoffee.api.events.departuresEvents;

import java.util.List;

import ch.liip.timeforcoffee.backend.Departure;

/**
 * Created by fsantschi on 11/03/15.
 */
public class ZvvDeparturesFetchedEvent {
    private final List<Departure> departures;

    public ZvvDeparturesFetchedEvent(List<Departure> departures) {
        this.departures = departures;
    }

    public List<Departure> getDepartures() {
        return departures;
    }
}
