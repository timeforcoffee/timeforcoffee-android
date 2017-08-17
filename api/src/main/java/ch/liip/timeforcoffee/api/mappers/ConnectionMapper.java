package ch.liip.timeforcoffee.api.mappers;

import ch.liip.timeforcoffee.api.Connection;

public class ConnectionMapper {

    public static Connection fromOpenData(ch.liip.timeforcoffee.opendata.Connection connection) {
        return new Connection(connection.getFrom() + " -> " + connection.getTo());
    }

}
