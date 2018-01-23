package ch.liip.timeforcoffee.api.events.connectionsEvents;

import java.util.List;

import ch.liip.timeforcoffee.api.models.Connection;

public class ConnectionsFetchedEvent {

    private final List<Connection> connections;

    public ConnectionsFetchedEvent(List<Connection> connections) {
        this.connections = connections;
    }

    public List<Connection> getConnections() {
        return connections;
    }

}
