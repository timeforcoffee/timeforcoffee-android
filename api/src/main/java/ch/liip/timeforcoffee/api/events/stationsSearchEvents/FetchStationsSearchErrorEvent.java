package ch.liip.timeforcoffee.api.events.stationsSearchEvents;

public class FetchStationsSearchErrorEvent {

    private Throwable throwable;

    public FetchStationsSearchErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
