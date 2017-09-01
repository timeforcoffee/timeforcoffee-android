package ch.liip.timeforcoffee.zvv;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by fsantschi on 09/03/15.
 */
public interface ZvvService {
    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/stations/{stationName}")
    public Observable<StationsResponse> getStations(@Path("stationName") String stationName);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/stationboard/{id}")
    public Observable<StationboardResponse> getStationboard(@Path("id") String id);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/connections/{from}/{to}/{start_time}/{end_time}")
    public Observable<ConnectionsResponse> getConnections(@Path("from") String fromStationId, @Path("to") String toStationId,
                                                          @Path("start_time") String startTimeStr, @Path("end_time")String endTimeStr);
}
