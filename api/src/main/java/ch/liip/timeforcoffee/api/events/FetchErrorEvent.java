package ch.liip.timeforcoffee.api.events;

import retrofit.RetrofitError;

/**
 * Created by nicolas on 06/03/16.
 */
public class FetchErrorEvent {
    private RetrofitError error;
    private Throwable throwable;

    public FetchErrorEvent(RetrofitError error) {
        this.error = error;
    }

    public FetchErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }
}
