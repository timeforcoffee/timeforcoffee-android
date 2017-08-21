package ch.liip.timeforcoffee.api;

import ch.liip.timeforcoffee.api.events.*;
import ch.liip.timeforcoffee.zvv.ConnectionsResponse;
import ch.liip.timeforcoffee.zvv.Station;
import ch.liip.timeforcoffee.zvv.StationboardResponse;
import ch.liip.timeforcoffee.zvv.StationsResponse;
import ch.liip.timeforcoffee.zvv.ZvvService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.inject.Inject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    public void onEvent(FetchZvvConnectionsEvent event) {
        fetchZvvConnections(event.getFromStationId(), event.getToStationId(), event.getStartDateStr(), event.getEndDateStr());
    }

    @Subscribe
    public void onEvent(FetchZvvStationboardEvent event) {
        fetchZvvStationboard(event.getStationId());
    }

    @Subscribe
    public void onEvent(FetchZvvStationsEvent event) {
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
                        eventBus.post(new FetchErrorEvent(e));
                    }

                    @Override
                    public void onNext(ConnectionsResponse connections) {
                        eventBus.post(new ZvvConnectionsFetchedEvent(connections.getCheckPoints()));
                    }
                });
    }

    public void fetchZvvStationboard(String stationId) {

        zvvService.getStationboard(stationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StationboardResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchErrorEvent(e));
                    }

                    @Override
                    public void onNext(StationboardResponse stationboard) {
                        eventBus.post(new ZvvStationboardFetchedEvent(stationboard.getDepartures()));
                    }
                });
    }

    public void fetchZvvStations(String searchQuery) {

        if (searchQuery.isEmpty()) { //search query is empty =>  return an empty station list
            List<Station> stations = new ArrayList<Station>();
            eventBus.post(new ZvvStationsFetchedEvent(stations));
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
                        eventBus.post(new FetchErrorEvent(e))   ;
                    }

                    @Override
                    public void onNext(StationsResponse stationsResponse) {
                        eventBus.post(new ZvvStationsFetchedEvent(stationsResponse.getStations()));
                    }
                });
    }
}
