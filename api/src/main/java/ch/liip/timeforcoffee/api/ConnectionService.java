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
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        crtMap.put("from", event.getStation().getName());
        crtMap.put("to", event.getDeparture().getDestination());
        crtMap.put("date", dateFormatter.format(event.getDeparture().getScheduled()));
        crtMap.put("time", timeFormatter.format(event.getDeparture().getScheduled()));

        eventBus.post(new FetchOpenDataConnectionsEvent(crtMap));
    }

    @Subscribe
    public void onEvent(OpenDataConnectionsFetchedEvent event) {
        List<Connection> connections = ConnectionMapper.fromOpenData(event.getConnections().get(0));
        eventBus.post(new ConnectionsFetchedEvent(connections));
    }
}
