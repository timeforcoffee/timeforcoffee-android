package ch.liip.timeforcoffee.api.events.stationsSearchEvents;

import retrofit.RetrofitError;

public class FetchStationsSearchErrorEvent {

    private RetrofitError error;
    private Throwable throwable;

    public FetchStationsSearchErrorEvent(RetrofitError error) {
        this.error = error;
    }

    public FetchStationsSearchErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }
}
