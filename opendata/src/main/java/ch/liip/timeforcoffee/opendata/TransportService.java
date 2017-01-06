package ch.liip.timeforcoffee.opendata;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.*;

/* Created by fsantschi on 08/03/15.
*/
public interface TransportService {
    @Headers("Cache-Control:public, max-age=20")
    @GET("/v1/connections")
    public void getConnections(@QueryMap Map<String,String> query, Callback<ConnectionsResponse> callback);
    @Headers("Cache-Control:public, max-age=20")
    @GET("/v1/locations")
    public void getLocations(@QueryMap Map<String,String> query, Callback<LocationsResponse> callback);
    @Headers("Cache-Control:public, max-age=20")
    @GET("/v1/stationboard")
    public void getStationboard(@QueryMap Map<String,String> query, Callback<StationboardResponse> callback);
}
