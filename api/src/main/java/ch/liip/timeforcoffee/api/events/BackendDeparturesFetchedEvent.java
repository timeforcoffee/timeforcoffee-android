package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.api.Departure;

public class BackendDeparturesFetchedEvent {

    private List<Departure> departures;

    public BackendDeparturesFetchedEvent(List<Departure> departures) {
        this.departures = departures;
    }

    public List<Departure> getDepartures() {
        return departures;
    }
}
