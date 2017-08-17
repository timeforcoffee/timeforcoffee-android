package ch.liip.timeforcoffee.api.events;

public class FetchConnectionsEvent {

    private final String from;
    private final String to;

    public FetchConnectionsEvent(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
