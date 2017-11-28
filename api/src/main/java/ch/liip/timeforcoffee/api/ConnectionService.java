package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.connectionsEvents.ConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.connectionsEvents.FetchConnectionsEvent;
import ch.liip.timeforcoffee.api.events.connectionsEvents.FetchZvvConnectionsEvent;
import ch.liip.timeforcoffee.api.events.connectionsEvents.ZvvConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.mappers.ConnectionMapper;
import ch.liip.timeforcoffee.api.models.Connection;

public class ConnectionService {

    private final EventBus eventBus;

    @Inject
    public ConnectionService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchConnectionsEvent event) {
        eventBus.post(new FetchZvvConnectionsEvent(event.getFromStationId(), event.getToStationId(), event.getStartDateStr(), event.getEndDateStr()));
    }

    @Subscribe
    public void onEvent(ZvvConnectionsFetchedEvent event) {
        List<Connection> connections = new ArrayList<>();
        for (ch.liip.timeforcoffee.zvv.CheckPoint zvvCheckPoint : event.getCheckPoints()) {
            connections.add(ConnectionMapper.fromZvv(zvvCheckPoint));
        }

        eventBus.post(new ConnectionsFetchedEvent(connections));
    }
}
