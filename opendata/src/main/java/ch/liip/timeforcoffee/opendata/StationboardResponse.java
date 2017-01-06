package ch.liip.timeforcoffee.opendata;

import java.util.List;

/**
 * Created by fsantschi on 09/03/15.
 */
public class StationboardResponse {
    private Connection station;
    private List<Journey> stationboards;

    public StationboardResponse() {}

    public Connection getStation() {
        return station;
    }

    public List<Journey> getStationboards() {
        return stationboards;
    }
}
