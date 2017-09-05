package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.BackendConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.ConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchBackendConnectionsEvent;
import ch.liip.timeforcoffee.api.events.FetchConnectionsEvent;

public class ConnectionService {

    private final EventBus eventBus;

    @Inject
    public ConnectionService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchConnectionsEvent event) {
        eventBus.post(new FetchBackendConnectionsEvent(
                event.getFromStationId(),
                event.getToStationId(),
                event.getDateStr(),
                event.getTimeStr()
        ));
    }

    @Subscribe
    public void onEvent(BackendConnectionsFetchedEvent event) {
        eventBus.post(new ConnectionsFetchedEvent(event.getConnections()));
    }
}
