package ch.liip.timeforcoffee.api.events.connectionsEvents;

public class FetchConnectionsEvent {

    private final int stationId;
    private final int destinationId;
    private final String departureString;

    public FetchConnectionsEvent(int stationId, int destinationId, String departureString) {
        this.stationId = stationId;
        this.destinationId = destinationId;
        this.departureString = departureString;
    }

    public int getStationId() {
        return stationId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public String getDepartureString() {
        return departureString;
    }
}
