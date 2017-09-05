package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.api.Station;

public class BackendLocationsFetchedEvent {

    private List<Station> stations;

    public BackendLocationsFetchedEvent(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
