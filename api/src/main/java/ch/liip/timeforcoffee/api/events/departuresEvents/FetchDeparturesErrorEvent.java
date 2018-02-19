package ch.liip.timeforcoffee.api.events.departuresEvents;

import retrofit.RetrofitError;

public class FetchDeparturesErrorEvent {

    private RetrofitError error;
    private Throwable throwable;

    public FetchDeparturesErrorEvent(RetrofitError error) {
        this.error = error;
    }

    public FetchDeparturesErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }
}
