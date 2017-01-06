package ch.liip.timeforcoffee.api;

import ch.liip.timeforcoffee.api.events.*;
import ch.liip.timeforcoffee.api.mappers.StationMapper;
import ch.liip.timeforcoffee.opendata.ConnectionsResponse;
import ch.liip.timeforcoffee.opendata.LocationsResponse;
import ch.liip.timeforcoffee.opendata.StationboardResponse;
import ch.liip.timeforcoffee.opendata.TransportService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by fsantschi on 08/03/15.
 */
public class OpenDataApiService {
    private EventBus eventBus;
    private TransportService transportService;

    @Inject
    public OpenDataApiService(EventBus eventBus, TransportService transportService) {
        this.eventBus = eventBus;
        this.transportService = transportService;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchOpenDataConnectionsEvent event) {
        fetchOpenDataConnections(event.getQuery());
    }

    @Subscribe
    public void onEvent(FetchOpenDataLocationsEvent event) {
        fetchOpenDataLocations(event.getQuery());
    }

    public void onEvent(FetchOpenDataStationboardEvent event) {
        fetchOpenDataStationboard(event.getQuery());
    }

    public void fetchOpenDataConnections(Map<String, String> query) {
        Callback<ConnectionsResponse> callback = new Callback<ConnectionsResponse>() {

            @Override
            public void failure(RetrofitError error) {
                eventBus.post(new FetchErrorEvent(error));
            }

            @Override
            public void success(ConnectionsResponse connections, Response response) {
                eventBus.post(new OpenDataConnectionsFetchedEvent(connections.getConnections()));
            }

        };

        transportService.getConnections(query, callback);
    }

    public void fetchOpenDataLocations(Map<String, String> query) {
        Callback<LocationsResponse> callback = new Callback<LocationsResponse>() {

            @Override
            public void failure(RetrofitError error) {

                eventBus.post(new FetchErrorEvent(error));
            }

            @Override
            public void success(LocationsResponse locations, Response response) {
                ArrayList<Station> stations = new ArrayList<Station>();
                for (ch.liip.timeforcoffee.opendata.Location location : locations.getStations()) {
                    stations.add(StationMapper.fromLocation(location));
                }
                eventBus.post(new StationsFetchedEvent(stations));
            }

        };

        transportService.getLocations(query, callback);
    }

    public void fetchOpenDataStationboard(Map<String, String> query) {
        Callback<StationboardResponse> callback = new Callback<StationboardResponse>() {

            @Override
            public void failure(RetrofitError error) {
                eventBus.post(new FetchErrorEvent(error));
            }

            @Override
            public void success(StationboardResponse stationboard, Response response) {
                eventBus.post(new OpenDataStationboardFetchedEvent(stationboard.getStationboards()));
            }

        };

        transportService.getStationboard(query, callback);
    }
}
