package ch.liip.timeforcoffee.api.events.stationsSearchEvents;

import java.util.List;

import ch.liip.timeforcoffee.api.models.Station;

public class StationsSearchFetchedEvent {

    private List<Station> stations;

    public StationsSearchFetchedEvent(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
