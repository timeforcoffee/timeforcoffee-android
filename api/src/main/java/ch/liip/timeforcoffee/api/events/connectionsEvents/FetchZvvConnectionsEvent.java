package ch.liip.timeforcoffee.api.events.connectionsEvents;

public class FetchZvvConnectionsEvent {

    private final String fromStationId;
    private final String toStationId;
    private final String startDateStr;
    private final String endDateStr;

    public FetchZvvConnectionsEvent(String fromStationId, String toStationId, String startDateStr, String endDateStr) {
        this.fromStationId = fromStationId;
        this.toStationId = toStationId;
        this.startDateStr = startDateStr;
        this.endDateStr = endDateStr;
    }

    public String getFromStationId() {
        return fromStationId;
    }

    public String getToStationId() {
        return toStationId;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }
}
