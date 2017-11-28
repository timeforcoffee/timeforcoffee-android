package ch.liip.timeforcoffee.api;

import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchOpenDataStationsLocationEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchStationsLocationErrorEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.OpenDataStationsLocationFetchedEvent;
import ch.liip.timeforcoffee.opendata.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.inject.Inject;

import java.util.Map;

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
    public void onEvent(FetchOpenDataStationsLocationEvent event) {
        fetchOpenDataLocations(event.getQuery());
    }

    private void fetchOpenDataLocations(Map<String, String> query) {

        transportService.getLocations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LocationsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchStationsLocationErrorEvent(e));
                    }

                    @Override
                    public void onNext(LocationsResponse locationsResponse) {
                        eventBus.post(new OpenDataStationsLocationFetchedEvent(locationsResponse.getStations()));
                    }
                });

    }
}
