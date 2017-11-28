package ch.liip.timeforcoffee.api.events.connectionsEvents;

import retrofit.RetrofitError;

public class FetchConnectionsErrorEvent {

    private RetrofitError error;
    private Throwable throwable;

    public FetchConnectionsErrorEvent(RetrofitError error) {
        this.error = error;
    }

    public FetchConnectionsErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }
}
