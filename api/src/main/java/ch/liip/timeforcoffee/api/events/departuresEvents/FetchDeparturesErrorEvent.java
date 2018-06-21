package ch.liip.timeforcoffee.api.events.departuresEvents;

public class FetchDeparturesErrorEvent {

    private Throwable throwable;

    public FetchDeparturesErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
