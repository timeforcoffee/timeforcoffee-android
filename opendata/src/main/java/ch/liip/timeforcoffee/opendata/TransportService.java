package ch.liip.timeforcoffee.opendata;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.QueryMap;
import rx.Observable;

import java.util.Map;

/* Created by fsantschi on 08/03/15.
*/
public interface TransportService {
    @Headers("Cache-Control:public, max-age=20")
    @GET("/v1/connections")
    public Observable<ConnectionsResponse> getConnections(@QueryMap Map<String, String> query);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/v1/locations")
    public Observable<LocationsResponse> getLocations(@QueryMap Map<String, String> query);

    @Headers("Cache-Control:public, max-age=20")
    @GET("/v1/stationboard")
    public Observable<StationboardResponse> getStationboard(@QueryMap Map<String, String> query);
}
