package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.ConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.DeparturesFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchConnectionsEvent;
import ch.liip.timeforcoffee.api.events.FetchOpenDataConnectionsEvent;
import ch.liip.timeforcoffee.api.events.FetchZvvConnectionsEvent;
import ch.liip.timeforcoffee.api.events.FetchZvvStationboardEvent;
import ch.liip.timeforcoffee.api.events.OpenDataConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.ZvvConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.ZvvStationboardFetchedEvent;
import ch.liip.timeforcoffee.api.mappers.ConnectionMapper;
import ch.liip.timeforcoffee.api.mappers.DepartureMapper;

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
