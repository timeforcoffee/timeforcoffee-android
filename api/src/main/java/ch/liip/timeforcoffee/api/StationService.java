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
import ch.liip.timeforcoffee.api.mappers.StationMapper;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.backend.OpenDataService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StationService {

    private final EventBus eventBus;
    private final OpenDataService openDataService;

    @Inject
    public StationService(EventBus eventBus, OpenDataService openDataService) {
        this.openDataService = openDataService;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchStationsSearchEvent event) {
        Map<String, String> query = new HashMap<>();
        query.put("type", "station");
        query.put("query", event.getSearchQuery());

        openDataService.getStations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ch.liip.timeforcoffee.backend.Stations>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchStationsSearchErrorEvent(e));
                    }

                    @Override
                    public void onNext(ch.liip.timeforcoffee.backend.Stations backendStations) {
                        ArrayList<Station> stations = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Station backendStation : backendStations.getStations()) {
                            stations.add(StationMapper.fromBackend(backendStation));
                        }

                        eventBus.post(new StationsSearchFetchedEvent(stations));
                    }
                });
    }

    @Subscribe
    public void onEvent(FetchStationsLocationEvent event) {
        Map<String, String> query = new HashMap<>();
        query.put("type", "station");
        query.put("x", Double.toString(event.getX()));
        query.put("y", Double.toString(event.getY()));

        openDataService.getStations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ch.liip.timeforcoffee.backend.Stations>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchStationsLocationErrorEvent(e));
                    }

                    @Override
                    public void onNext(ch.liip.timeforcoffee.backend.Stations backendStations) {
                        ArrayList<Station> stations = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Station backendStation : backendStations.getStations()) {
                            if (backendStation.getId() == 0) {
                                //the API returns a station with null id corresponding to current location, we exclude it
                                continue;
                            }
                            stations.add(StationMapper.fromBackend(backendStation));
                        }

                        eventBus.post(new StationsLocationFetchedEvent(stations));
                    }
                });
    }
}
