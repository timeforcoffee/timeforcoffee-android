package ch.liip.timeforcoffee.backend;

import java.util.List;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.QueryMap;
import rx.Observable;

public interface BackendService {

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/stationboard")
    Observable<List<Departure>> getDepartures(@QueryMap Map<String, String> query);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/connections")
    Observable<List<Connection>> getConnections(@QueryMap Map<String, String> query);
}
