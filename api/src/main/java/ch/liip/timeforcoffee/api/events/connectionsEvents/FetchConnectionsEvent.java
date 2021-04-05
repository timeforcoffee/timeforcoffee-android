package ch.liip.timeforcoffee.api.events.connectionsEvents;

public class FetchConnectionsEvent {

    private final int stationId;
    private final int destinationId;
    private final String departureString;
    private final String arrivalString;

    public FetchConnectionsEvent(int stationId, int destinationId, String departureString, String arrivalString) {
        this.stationId = stationId;
        this.destinationId = destinationId;
        this.departureString = departureString;
        this.arrivalString = arrivalString;
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

    public String getArrivalString() {
        return arrivalString;
    }
}
