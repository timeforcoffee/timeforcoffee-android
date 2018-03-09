package ch.liip.timeforcoffee.zvv;

import com.google.gson.annotations.SerializedName;

public class StationboardMeta {

    @SerializedName("station_id") private String stationId;
    @SerializedName("station_name") private String stationName;

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
