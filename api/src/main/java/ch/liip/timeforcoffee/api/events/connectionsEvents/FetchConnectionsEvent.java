package ch.liip.timeforcoffee.api.events.connectionsEvents;

public class FetchConnectionsEvent {

    private final String stationId;
    private final String destinationId;
    private final String departureString;

    public FetchConnectionsEvent(String stationId, String destinationId, String departureString) {
        this.stationId = stationId;
        this.destinationId = destinationId;
        this.departureString = departureString;
    }

    public String getStationId() {
        return stationId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public String getDepartureString() {
        return departureString;
    }
}
