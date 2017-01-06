package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.zvv.Station;

/**
 * Created by fsantschi on 11/03/15.
 */
public class ZvvStationsFetchedEvent {

    private final List<Station> stations;

    public ZvvStationsFetchedEvent(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
