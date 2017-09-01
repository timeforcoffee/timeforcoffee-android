package ch.liip.timeforcoffee.opendata;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.QueryMap;
import rx.Observable;

import java.util.Map;

public interface TransportService {

    @Headers("Cache-Control:public, max-age=20")
    @GET("/v1/locations")
    Observable<LocationsResponse> getLocations(@QueryMap Map<String, String> query);

}
