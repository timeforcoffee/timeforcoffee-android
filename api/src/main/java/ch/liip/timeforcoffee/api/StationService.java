package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchStationsLocationErrorEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchStationsLocationEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.StationsLocationFetchedEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchStationsSearchErrorEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.FetchStationsSearchEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchEvents.StationsSearchFetchedEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchOneEvents.FetchStationsOneSearchErrorEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchOneEvents.FetchStationsSearchOneEvent;
import ch.liip.timeforcoffee.api.events.stationsSearchOneEvents.StationsSearchOneFetchedEvent;
import ch.liip.timeforcoffee.api.mappers.StationMapper;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.backend.BackendService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StationService {

    private final EventBus eventBus;
    private final BackendService backendService;

    @Inject
    public StationService(EventBus eventBus, BackendService backendService) {
        this.backendService = backendService;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchStationsSearchEvent event) {
        Map<String, String> query = new HashMap<>();
        query.put("query", event.getSearchQuery());

        backendService.getStations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ch.liip.timeforcoffee.backend.Station>>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchStationsSearchErrorEvent(e));
                    }

                    @Override
                    public void onNext(List<ch.liip.timeforcoffee.backend.Station> backendStations) {
                        ArrayList<Station> stations = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Station backendStation : backendStations) {
                            stations.add(StationMapper.fromBackend(backendStation));
                        }

                        eventBus.post(new StationsSearchFetchedEvent(stations));
                    }
                });
    }

    @Subscribe
    public void onEvent(FetchStationsSearchOneEvent event) {
        if (event.getSearchQuery().isEmpty()) {
            eventBus.post(new StationsSearchOneFetchedEvent(null));
            return;
        }

        Map<String, String> query = new HashMap<>();
        query.put("query", event.getSearchQuery());

        backendService.getStations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ch.liip.timeforcoffee.backend.Station>>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchStationsOneSearchErrorEvent(e))   ;
                    }

                    @Override
                    public void onNext(List<ch.liip.timeforcoffee.backend.Station> bakendStations) {
                        Station station = StationMapper.fromBackend(bakendStations.get(0));
                        eventBus.post(new StationsSearchOneFetchedEvent(station));
                    }
                });
    }

    @Subscribe
    public void onEvent(FetchStationsLocationEvent event) {
        Map<String, String> query = new HashMap<>();
        query.put("x", Double.toString(event.getX()));
        query.put("y", Double.toString(event.getY()));

        backendService.getStations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ch.liip.timeforcoffee.backend.Station>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchStationsLocationErrorEvent(e));
                    }

                    @Override
                    public void onNext(List<ch.liip.timeforcoffee.backend.Station> backendStations) {
                        ArrayList<Station> stations = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Station backendStation : backendStations) {
                            stations.add(StationMapper.fromBackend(backendStation));
                        }

                        eventBus.post(new StationsLocationFetchedEvent(stations));
                    }
                });
    }
}
