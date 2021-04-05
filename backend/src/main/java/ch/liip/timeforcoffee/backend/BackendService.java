package ch.liip.timeforcoffee.backend;

import java.util.List;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import rx.Observable;

public interface BackendService {

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/stationboard/{stationId}")
    Observable<Departures> getDepartures(@Path("stationId") String stationId);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/connections/{stationId}/{destinationId}/{departureDate}/{arrivalDate}")
    Observable<Connections> getConnections(
            @Path("stationId") String stationId,
            @Path("destinationId") String destinationId,
            @Path("departureDate") String departureDate,
            @Path("arrivalDate") String arrivalDate);
}
