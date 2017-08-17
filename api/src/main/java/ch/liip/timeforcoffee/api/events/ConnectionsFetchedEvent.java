package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.api.Connection;

public class ConnectionsFetchedEvent {

    private final List<Connection> connections;

    public ConnectionsFetchedEvent(List<Connection> connections) {
        this.connections = connections;
    }

    public List<Connection> getConnections() {
        return connections;
    }

}
