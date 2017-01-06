package ch.liip.timeforcoffee.api.events;

import retrofit.RetrofitError;

/**
 * Created by nicolas on 06/03/16.
 */
public class FetchErrorEvent {
    private RetrofitError error;
    public FetchErrorEvent(RetrofitError error) {
        this.error = error;
    }
}
