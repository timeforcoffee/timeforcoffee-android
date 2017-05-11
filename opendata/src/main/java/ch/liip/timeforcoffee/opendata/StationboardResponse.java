package ch.liip.timeforcoffee.opendata;

import java.util.List;

/**
 * Created by fsantschi on 09/03/15.
 */
public class StationboardResponse {
    private Location station;
    private List<Journey> stationboard;

    public StationboardResponse() {}

    public Location getStation() {
        return station;
    }

    public List<Journey> getStationboards() {
        return stationboard;
    }
}
