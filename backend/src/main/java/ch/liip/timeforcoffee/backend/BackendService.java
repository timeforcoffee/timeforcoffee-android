package ch.liip.timeforcoffee.backend;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;

public interface BackendService {

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/locations")
    Observable<List<Station>> getLocations(@Query("x") String x, @Query("y") String y);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/stations")
    Observable<List<Station>> getStations(@Query("query") String query);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/stationboard")
    Observable<List<Journey>> getStationboard(@Query("station_id") String stationId);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/connections")
    Observable<List<Connection>> getConnections(
            @Query("station_id") String stationId,
            @Query("destination_id") String destinationId,
            @Query("date") String date,
            @Query("time") String time
    );

}
