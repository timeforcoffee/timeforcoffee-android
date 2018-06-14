package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.departuresEvents.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesErrorEvent;
import ch.liip.timeforcoffee.api.events.departuresEvents.FetchDeparturesEvent;
import ch.liip.timeforcoffee.api.mappers.DepartureMapper;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.backend.BackendService;
import ch.liip.timeforcoffee.backend.StationboardResponse;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DepartureService {

    private final EventBus eventBus;
    private final BackendService backendService;

    @Inject
    public DepartureService(EventBus eventBus, BackendService backendService) {
        this.backendService = backendService;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchDeparturesEvent event) {
        Map<String, String> query = new HashMap<>();
        query.put("station_id", event.getStationId());

        backendService.getDepartures(query)
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
                    public void onNext(StationboardResponse stationboardResponse) {
                        ArrayList<Departure> departures = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Departure backendDeparture : stationboardResponse.getDepartures()) {
                            departures.add(DepartureMapper.fromBackend(backendDeparture));
                        }

                        eventBus.post(new DeparturesFetchedEvent(departures));
                    }
                });

    }
}
