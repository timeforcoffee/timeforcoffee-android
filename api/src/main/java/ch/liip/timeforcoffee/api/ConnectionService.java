package ch.liip.timeforcoffee.api;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ch.liip.timeforcoffee.api.events.ConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.events.FetchConnectionsEvent;
import ch.liip.timeforcoffee.api.events.FetchOpenDataConnectionsEvent;
import ch.liip.timeforcoffee.api.events.OpenDataConnectionsFetchedEvent;
import ch.liip.timeforcoffee.api.mappers.ConnectionMapper;

public class ConnectionService {

    private final EventBus eventBus;

    @Inject
    public ConnectionService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(FetchConnectionsEvent event) {
        Map<String,String> crtMap = new HashMap<>();
        crtMap.put("from", event.getDeparture().getDestination());
        crtMap.put("to", event.getDeparture().getDestination());
        eventBus.post(new FetchOpenDataConnectionsEvent(crtMap));
    }

    @Subscribe
    public void onEvent(OpenDataConnectionsFetchedEvent event) {
        ArrayList<Connection> connections = new ArrayList<>();
        for(ch.liip.timeforcoffee.opendata.Connection connection : event.getConnections()) {
            connections.add(ConnectionMapper.fromOpenData(connection));
        }

        eventBus.post(new ConnectionsFetchedEvent(connections));
    }
}
