package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.connectionsEvents.FetchConnectionsErrorEvent;
import ch.liip.timeforcoffee.api.events.connectionsEvents.FetchZvvConnectionsEvent;
import ch.liip.timeforcoffee.api.events.connectionsEvents.ZvvConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesErrorEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchZvvDeparturesEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.ZvvDeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchStationsSearchErrorEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchZvvStationsSearchEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.ZvvStationsSearchFetchedEvent;
import ch.liip.timeforcoffee.zvv.ConnectionsResponse;
import ch.liip.timeforcoffee.zvv.Station;
import ch.liip.timeforcoffee.zvv.StationboardResponse;
import ch.liip.timeforcoffee.zvv.StationsResponse;
import ch.liip.timeforcoffee.zvv.ZvvService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    public void onEvent(FetchZvvConnectionsEvent event) {
        fetchZvvConnections(event.getFromStationId(), event.getToStationId(), event.getStartDateStr(), event.getEndDateStr());
    }

    @Subscribe
    public void onEvent(FetchZvvDeparturesEvent event) {
        fetchZvvDepartures(event.getStationId());
    }

    @Subscribe
    public void onEvent(FetchZvvStationsSearchEvent event) {
        fetchZvvStations(event.getSearchQuery());
    }

    public void fetchZvvConnections(String fromStationId, String toStationId, String startDateStr, String endDateStr) {

        zvvService.getConnections(fromStationId, toStationId, startDateStr, endDateStr)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ConnectionsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchConnectionsErrorEvent(e));
                    }

                    @Override
                    public void onNext(ConnectionsResponse connections) {
                        eventBus.post(new ZvvConnectionsFetchedEvent(connections.getConnections()));
                    }
                });
    }

    public void fetchZvvDepartures(String stationId) {

        zvvService.getDepartures(stationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StationboardResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchDeparturesErrorEvent(e));
                    }

                    @Override
                    public void onNext(StationboardResponse stationboard) {
                        eventBus.post(new ZvvDeparturesFetchedEvent(stationboard.getDepartures()));
                    }
                });
    }

    public void fetchZvvStations(String searchQuery) {

        if (searchQuery.isEmpty()) { //search query is empty =>  return an empty station list
            List<Station> stations = new ArrayList<Station>();
            eventBus.post(new ZvvStationsSearchFetchedEvent(stations));
            return;
        }

        zvvService.getStations(searchQuery)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StationsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchStationsSearchErrorEvent(e))   ;
                    }

                    @Override
                    public void onNext(StationsResponse stationsResponse) {
                        eventBus.post(new ZvvStationsSearchFetchedEvent(stationsResponse.getStations()));
                    }
                });
    }
}
