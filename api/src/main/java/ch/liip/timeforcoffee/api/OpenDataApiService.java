package ch.liip.timeforcoffee.api;

import ch.liip.timeforcoffee.api.events.*;
import ch.liip.timeforcoffee.api.mappers.StationMapper;
import ch.liip.timeforcoffee.opendata.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.ArrayList;
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
    public void onEvent(FetchOpenDataLocationsEvent event) {
        fetchOpenDataLocations(event.getQuery());
    }

    public void fetchOpenDataLocations(Map<String, String> query) {

        transportService.getLocations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<LocationsResponse, ArrayList<Station>>() {
                    @Override
                    public ArrayList<Station> call(LocationsResponse locations) {
                        ArrayList<Station> stations = new ArrayList<Station>();
                        for (ch.liip.timeforcoffee.opendata.Location location : locations.getStations()) {
                            Station newStation = StationMapper.fromLocation(location);
                            if (newStation != null) {
                                stations.add(newStation);
                            }
                        }
                        return stations;
                    }
                })
                .subscribe(new Subscriber<ArrayList<Station>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventBus.post(new FetchErrorEvent(e));
                    }

                    @Override
                    public void onNext(ArrayList<Station> stations) {
                        eventBus.post(new StationsFetchedEvent(stations));
                    }
                });

    }
}
