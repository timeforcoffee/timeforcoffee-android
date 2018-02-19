package ch.liip.timeforcoffee.api.events.stationsLocationEvents;

import retrofit.RetrofitError;

public class FetchStationsLocationErrorEvent {

    private RetrofitError error;
    private Throwable throwable;

    public FetchStationsLocationErrorEvent(RetrofitError error) {
        this.error = error;
    }

    public FetchStationsLocationErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }
}
