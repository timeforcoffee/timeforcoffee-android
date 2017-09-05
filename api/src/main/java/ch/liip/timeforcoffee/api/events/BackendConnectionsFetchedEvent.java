package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.api.Connection;

public class BackendConnectionsFetchedEvent {

    private List<Connection> connections;

    public BackendConnectionsFetchedEvent(List<Connection> connections) {
        this.connections = connections;
    }

    public List<Connection> getConnections() {
        return connections;
    }
}
