package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.opendata.Connection;

/**
 * Created by fsantschi on 08/03/15.
 */
public class OpenDataConnectionsFetchedEvent {
    private List<Connection> connections;

    public OpenDataConnectionsFetchedEvent(List<Connection> connections) {
        this.connections = connections;
    }
}
