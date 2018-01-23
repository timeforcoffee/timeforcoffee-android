package ch.liip.timeforcoffee.api.events.stationsLocationEvents;

import java.util.List;

import ch.liip.timeforcoffee.api.models.Station;

public class StationsLocationFetchedEvent {

    private List<Station> stations;

    public StationsLocationFetchedEvent(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
