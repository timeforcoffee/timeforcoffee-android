package ch.liip.timeforcoffee.opendata;

import java.util.List;

/**
 * Created by fsantschi on 08/03/15.
 */
public class LocationsResponse {
    private List<Location> stations;

    LocationsResponse() {}

    public List<Location> getStations() {
        return stations;
    }
}
