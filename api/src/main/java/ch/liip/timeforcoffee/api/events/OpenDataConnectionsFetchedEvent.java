package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.opendata.Connection;

public class OpenDataConnectionsFetchedEvent {

    private List<Connection> connections;

    public OpenDataConnectionsFetchedEvent(List<Connection> connections) {
        this.connections = connections;
    }

    public List<Connection> getConnections() {
        return connections;
    }
}
