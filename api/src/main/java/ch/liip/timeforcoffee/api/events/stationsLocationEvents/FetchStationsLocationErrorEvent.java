package ch.liip.timeforcoffee.api.events.stationsLocationEvents;

public class FetchStationsLocationErrorEvent {

    private Throwable throwable;

    public FetchStationsLocationErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
