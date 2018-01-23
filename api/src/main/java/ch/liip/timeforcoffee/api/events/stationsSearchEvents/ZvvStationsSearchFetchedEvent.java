package ch.liip.timeforcoffee.api.events.stationsSearchEvents;

import java.util.List;

import ch.liip.timeforcoffee.zvv.Station;

/**
 * Created by fsantschi on 11/03/15.
 */
public class ZvvStationsSearchFetchedEvent {

    private final List<Station> stations;

    public ZvvStationsSearchFetchedEvent(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
