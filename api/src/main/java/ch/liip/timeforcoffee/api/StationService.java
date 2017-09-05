package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.BackendLocationsFetchedEvent;
import ch.liip.timeforcoffee.api.events.BackendStationsFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchBackendLocationsEvent;
import ch.liip.timeforcoffee.api.events.FetchBackendStationsEvent;
import ch.liip.timeforcoffee.api.events.FetchLocationsEvent;
import ch.liip.timeforcoffee.api.events.FetchStationsEvent;
import ch.liip.timeforcoffee.api.events.LocationsFetchedEvent;
import ch.liip.timeforcoffee.api.events.StationsFetchedEvent;

public class StationService {

    private final EventBus eventBus;

    @Inject
    public StationService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchStationsEvent event) {
        eventBus.post(new FetchBackendStationsEvent(event.getSearchQuery()));
    }

    @Subscribe
    public void onEvent(FetchLocationsEvent event) {
        eventBus.post(new FetchBackendLocationsEvent(event.getX(), event.getY()));
    }

    @Subscribe
    public void onEvent(BackendStationsFetchedEvent event) {
        eventBus.post(new StationsFetchedEvent(event.getStations()));
    }

    @Subscribe
    public void onEvent(BackendLocationsFetchedEvent event) {
        eventBus.post(new LocationsFetchedEvent(event.getStations()));
    }
}
