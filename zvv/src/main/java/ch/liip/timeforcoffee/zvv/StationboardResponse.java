package ch.liip.timeforcoffee.zvv;

import java.util.List;

public class StationboardResponse {

    private List<Departure> departures;
    private StationboardMeta meta;

    public StationboardResponse() {}

    public List<Departure> getDepartures() {
        return departures;
    }

    public StationboardMeta getMeta() {
        return meta;
    }
}
