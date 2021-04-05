package ch.liip.timeforcoffee.backend;

import java.util.List;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.QueryMap;
import rx.Observable;

public interface OpenDataService {

    @Headers("Cache-Control:public, max-age=20")
    @GET("/v1/locations")
    Observable<Stations> getStations(@QueryMap Map<String, String> query);
}
