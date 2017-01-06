package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.opendata.Location;

/**
 * Created by fsantschi on 08/03/15.
 */
public class OpenDataLocationsFetchedEvent {
    private List<Location> locations;

    public OpenDataLocationsFetchedEvent(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
