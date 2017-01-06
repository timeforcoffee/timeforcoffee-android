package ch.liip.timeforcoffee.zvv;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by fsantschi on 09/03/15.
 */
public interface ZvvService {
    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/stations/{stationName}")
    public void getStations(@Path("stationName") String stationName, Callback<StationsResponse> callback);
    @Headers("Cache-Control:public, max-age=20")
    @GET("/api/ch/stationboard/{id}")
    public void getStationboard(@Path("id") String id, Callback<StationboardResponse> callback);
}
