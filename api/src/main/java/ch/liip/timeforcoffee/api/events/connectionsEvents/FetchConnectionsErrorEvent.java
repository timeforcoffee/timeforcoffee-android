package ch.liip.timeforcoffee.api.events.connectionsEvents;

public class FetchConnectionsErrorEvent {

    private Throwable throwable;

    public FetchConnectionsErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
