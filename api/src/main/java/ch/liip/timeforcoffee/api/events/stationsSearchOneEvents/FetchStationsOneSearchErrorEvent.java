package ch.liip.timeforcoffee.api.events.stationsSearchOneEvents;

import retrofit.RetrofitError;

public class FetchStationsOneSearchErrorEvent {

    private RetrofitError error;
    private Throwable throwable;

    public FetchStationsOneSearchErrorEvent(RetrofitError error) {
        this.error = error;
    }

    public FetchStationsOneSearchErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }
}
