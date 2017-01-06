package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.api.Station;

/**
 * Created by fsantschi on 11/03/15.
 */
public class StationsFetchedEvent {
    private List<Station> stations;

    public StationsFetchedEvent(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
