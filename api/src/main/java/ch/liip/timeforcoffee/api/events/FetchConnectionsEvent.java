package ch.liip.timeforcoffee.api.events;

import ch.liip.timeforcoffee.api.Departure;

public class FetchConnectionsEvent {

    private final Departure departure;

    public FetchConnectionsEvent(Departure departure) {
        this.departure = departure;
    }

    public Departure getDeparture() {
        return departure;
    }

}
