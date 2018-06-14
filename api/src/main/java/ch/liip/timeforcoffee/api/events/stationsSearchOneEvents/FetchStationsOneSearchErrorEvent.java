package ch.liip.timeforcoffee.api.events.stationsSearchOneEvents;

public class FetchStationsOneSearchErrorEvent {

    private Throwable throwable;

    public FetchStationsOneSearchErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
