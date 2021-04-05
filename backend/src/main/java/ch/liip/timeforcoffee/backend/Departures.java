package ch.liip.timeforcoffee.backend;

import java.util.List;

public class Departures {

    private List<Departure> departures;

    private Meta meta;

    public Departures() {}

    public List<Departure> getDepartures() {
        return departures;
    }

    public Meta getMeta() { return meta; }
}
