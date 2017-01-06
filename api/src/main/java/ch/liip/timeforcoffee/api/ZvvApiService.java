package ch.liip.timeforcoffee.api;

import ch.liip.timeforcoffee.api.events.*;
import ch.liip.timeforcoffee.zvv.Station;
import ch.liip.timeforcoffee.zvv.StationboardResponse;
import ch.liip.timeforcoffee.zvv.StationsResponse;
import ch.liip.timeforcoffee.zvv.ZvvService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fsantschi on 08/03/15.
 */
public class ZvvApiService {
    private EventBus eventBus;
    private ZvvService zvvService;

    @Inject
    public ZvvApiService(EventBus eventBus, ZvvService zvvService) {
        this.eventBus = eventBus;
        this.zvvService = zvvService;
        this.eventBus.register(this);
    }
    
    @Subscribe
    public void onEvent(FetchZvvStationboardEvent event) {
        fetchZvvStationboard(event.getStationId());
    }

    @Subscribe
    public void onEvent(FetchZvvStationsEvent event) {
        fetchZvvStations(event.getSearchQuery());
    }

    public void fetchZvvStationboard(String stationId) {
        Callback<StationboardResponse> callback = new Callback<StationboardResponse>() {

            @Override
            public void failure(RetrofitError error) {
                eventBus.post(new FetchErrorEvent(error));
            }

            @Override
            public void success(StationboardResponse stationboard, Response response) {
                eventBus.post(new ZvvStationboardFetchedEvent(stationboard.getDepartures()));
            }

        };

        zvvService.getStationboard(stationId, callback);
    }

    public void fetchZvvStations(String searchQuery) {
        Callback<StationsResponse> callback = new Callback<StationsResponse>() {

            @Override
            public void failure(RetrofitError error) {
                eventBus.post(new FetchErrorEvent(error));
            }

            @Override
            public void success(StationsResponse stationsResponse, Response response) {
                eventBus.post(new ZvvStationsFetchedEvent(stationsResponse.getStations()));
            }
        };

        if (searchQuery.isEmpty()) { //search query is empty =>  return an empty station list
            List<Station> stations = new ArrayList<Station>();
            eventBus.post(new ZvvStationsFetchedEvent(stations));
            return;
        }

        zvvService.getStations(searchQuery, callback);
    }
}
