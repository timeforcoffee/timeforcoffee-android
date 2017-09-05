package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.api.Station;

public class LocationsFetchedEvent {

    private List<Station> stations;

    public LocationsFetchedEvent(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
