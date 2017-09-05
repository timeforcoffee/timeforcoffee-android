package ch.liip.timeforcoffee.api.events;

public class FetchBackendConnectionsEvent {

    private String stationId;
    private String destinationId;
    private String date;
    private String time;

    public FetchBackendConnectionsEvent(String stationId, String destinationId, String date, String time) {
        this.stationId = stationId;
        this.destinationId = destinationId;
        this.date = date;
        this.time = time;
    }

    public String getStationId() {
        return stationId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
