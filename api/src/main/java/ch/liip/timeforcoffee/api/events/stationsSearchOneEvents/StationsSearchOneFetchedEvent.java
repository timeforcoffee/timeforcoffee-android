package ch.liip.timeforcoffee.api.events.stationsSearchOneEvents;

import ch.liip.timeforcoffee.api.models.Station;

public class StationsSearchOneFetchedEvent {

    private Station station;

    public StationsSearchOneFetchedEvent(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
