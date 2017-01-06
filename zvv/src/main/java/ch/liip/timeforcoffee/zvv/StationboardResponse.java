package ch.liip.timeforcoffee.zvv;

import java.util.List;

/**
 * Created by fsantschi on 09/03/15.
 */
public class StationboardResponse {
    private List<Departure> departures;
    private StationboardMeta meta;

    public StationboardResponse() {};

    public List<Departure> getDepartures() {
        return departures;
    }

    public StationboardMeta getMeta() {
        return meta;
    }
}
