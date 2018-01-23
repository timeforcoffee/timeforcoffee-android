package ch.liip.timeforcoffee.api.events.stationsSearchOneEvents;

import ch.liip.timeforcoffee.zvv.Station;

public class ZvvStationsSearchOneFetchedEvent {

    private Station station;

    public ZvvStationsSearchOneFetchedEvent(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
