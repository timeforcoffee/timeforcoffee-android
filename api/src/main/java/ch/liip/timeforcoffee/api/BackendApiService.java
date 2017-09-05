package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.BackendConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.BackendDeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.BackendLocationsFetchedEvent;
import ch.liip.timeforcoffee.api.events.BackendStationsFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchBackendConnectionsEvent;
import ch.liip.timeforcoffee.api.events.FetchBackendDeparturesEvent;
import ch.liip.timeforcoffee.api.events.FetchBackendLocationsEvent;
import ch.liip.timeforcoffee.api.events.FetchBackendStationsEvent;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.api.mappers.ConnectionMapper;
import ch.liip.timeforcoffee.api.mappers.DepartureMapper;
import ch.liip.timeforcoffee.api.mappers.StationMapper;
import ch.liip.timeforcoffee.backend.BackendService;
import ch.liip.timeforcoffee.backend.Journey;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

// handle injection
public class BackendApiService {

    private EventBus eventBus;
    private BackendService backendService;

    @Inject
    public BackendApiService(EventBus eventBus, BackendService backendService) {
        this.backendService = backendService;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }


    // Events

    @Subscribe
    public void onEvent(FetchBackendStationsEvent event) {
        fetchBackendStations(event.getQuery());
    }

    @Subscribe
    public void onEvent(FetchBackendLocationsEvent event) {
        fetchBackendLocations(event.getX(), event.getY());
    }

    @Subscribe
    public void onEvent(FetchBackendDeparturesEvent event) {
        fetchBackendDepartures(event.getStationId());
    }

    @Subscribe
    public void onEvent(FetchBackendConnectionsEvent event) {
        fetchBackendConnections(event.getStationId(), event.getDestinationId(), event.getDate(), event.getTime());
    }


    // Fetch data

    private void fetchBackendStations(String query) {

        backendService.getStations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<ch.liip.timeforcoffee.backend.Station>, ArrayList<Station>>() {
                    @Override
                    public ArrayList<Station> call(List<ch.liip.timeforcoffee.backend.Station> stations) {
                        ArrayList<Station> finalStations = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Station station: stations) {
                            finalStations.add(StationMapper.fromBackend(station));
                        }

                        return finalStations;
                    }
                })
                .subscribe(new Subscriber<ArrayList<Station>>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchErrorEvent(e));
                    }

                    @Override
                    public void onNext(ArrayList<Station> stations) {
                        eventBus.post(new BackendStationsFetchedEvent(stations));
                    }
                });
    }

    private void fetchBackendLocations(String x, String y) {

        backendService.getLocations(x, y)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<ch.liip.timeforcoffee.backend.Station>, ArrayList<Station>>() {
                    @Override
                    public ArrayList<Station> call(List<ch.liip.timeforcoffee.backend.Station> stations) {
                        ArrayList<Station> finalStations = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Station station: stations) {
                            finalStations.add(StationMapper.fromBackend(station));
                        }

                        return finalStations;
                    }
                })
                .subscribe(new Subscriber<ArrayList<Station>>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchErrorEvent(e));
                    }

                    @Override
                    public void onNext(ArrayList<Station> stations) {
                        eventBus.post(new BackendLocationsFetchedEvent(stations));
                    }
                });
    }

    private void fetchBackendDepartures(String stationId) {

        backendService.getStationboard(stationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<Journey>, ArrayList<Departure>>() {
                    @Override
                    public ArrayList<Departure> call(List<Journey> journeys) {
                        ArrayList<Departure> departures = new ArrayList<>();
                        for (Journey journey : journeys) {
                            departures.add(DepartureMapper.fromBackend(journey));
                        }

                        return departures;
                    }
                })
                .subscribe(new Subscriber<ArrayList<Departure>>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchErrorEvent(e));
                    }

                    @Override
                    public void onNext(ArrayList<Departure> departures) {
                        eventBus.post(new BackendDeparturesFetchedEvent(departures));
                    }
                });
    }

    private void fetchBackendConnections(String stationId, String destinationId, String date, String time) {

        backendService.getConnections(stationId, destinationId, date, time)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<ch.liip.timeforcoffee.backend.Connection>, ArrayList<Connection>>() {
                    @Override
                    public ArrayList<Connection> call(List<ch.liip.timeforcoffee.backend.Connection> connections) {
                        ArrayList<Connection> finalConnections = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Connection connection : connections) {
                            finalConnections.add(ConnectionMapper.fromBackend(connection));
                        }

                        return finalConnections;
                    }
                })
                .subscribe(new Subscriber<ArrayList<Connection>>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchErrorEvent(e));
                    }

                    @Override
                    public void onNext(ArrayList<Connection> connections) {
                        eventBus.post(new BackendConnectionsFetchedEvent(connections));
                    }
                });
    }
}
