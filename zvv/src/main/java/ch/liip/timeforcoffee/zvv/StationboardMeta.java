package ch.liip.timeforcoffee.zvv;

public class StationboardMeta {

    private String stationId;
    private String stationName;

    public StationboardMeta(String stationId, String stationName ) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }
}
