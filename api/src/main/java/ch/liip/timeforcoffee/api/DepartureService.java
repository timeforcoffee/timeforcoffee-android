package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.BackendDeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchBackendDeparturesEvent;
import ch.liip.timeforcoffee.api.events.FetchDeparturesEvent;

public class DepartureService {

    private final EventBus eventBus;

    @Inject
    public DepartureService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchDeparturesEvent event) {
        eventBus.post(new FetchBackendDeparturesEvent(event.getStationId()));
    }

    @Subscribe
    public void onEvent(BackendDeparturesFetchedEvent event) {
        eventBus.post(new DeparturesFetchedEvent(event.getDepartures()));
    }
}
