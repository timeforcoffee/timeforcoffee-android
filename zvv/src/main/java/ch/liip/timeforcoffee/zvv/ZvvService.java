package ch.liip.timeforcoffee.zvv;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

public interface ZvvService {

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/stations/{stationName}")
    Observable<StationsResponse> getStations(@Path("stationName") String stationName);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/stationboard/{id}")
    Observable<StationboardResponse> getDepartures(@Path("id") String id);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/connections/{from}/{to}/{start_time}/{end_time}")
    Observable<ConnectionsResponse> getConnections(
            @Path("from") String fromStationId,
            @Path("to") String toStationId,
            @Path("start_time") String startTimeStr,
            @Path("end_time") String endTimeStr
    );
}
