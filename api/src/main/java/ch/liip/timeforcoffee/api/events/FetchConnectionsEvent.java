package ch.liip.timeforcoffee.api.events;

public class FetchConnectionsEvent {

    private final String fromStationId;
    private final String toStationId;
    private final String dateStr;
    private final String timeStr;

    public FetchConnectionsEvent(String fromStationId, String toStationId, String dateStr, String timeStr) {
        this.fromStationId = fromStationId;
        this.toStationId = toStationId;
        this.dateStr = dateStr;
        this.timeStr = timeStr;
    }

    public String getFromStationId() {
        return fromStationId;
    }

    public String getToStationId() {
        return toStationId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getTimeStr() {
        return timeStr;
    }
}
