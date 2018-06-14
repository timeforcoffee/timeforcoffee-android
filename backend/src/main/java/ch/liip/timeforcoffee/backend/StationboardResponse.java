package ch.liip.timeforcoffee.backend;

import java.util.List;

public class StationboardResponse {

    private List<Departure> departures;

    public StationboardResponse() {}

    public List<Departure> getDepartures() {
        return departures;
    }
}
