package ch.liip.timeforcoffee.api.events.stationsLocationEvents;

import java.util.List;

import ch.liip.timeforcoffee.opendata.Location;

public class OpenDataStationsLocationFetchedEvent {

    private List<Location> locations;

    public OpenDataStationsLocationFetchedEvent(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
