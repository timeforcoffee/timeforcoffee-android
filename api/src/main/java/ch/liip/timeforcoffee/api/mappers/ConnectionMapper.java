package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import ch.liip.timeforcoffee.api.Connection;

public class ConnectionMapper {

    public static Connection fromBackend(ch.liip.timeforcoffee.backend.Connection connection) {
        Location location = new Location("reverseGeocoded");
        location.setLongitude(connection.getCoordinate().getX());
        location.setLatitude(connection.getCoordinate().getY());

        return new Connection(
                connection.getId(),
                connection.getName(),
                location,
                connection.getDeparture().getScheduled(),
                connection.getDeparture().getRealtime(),
                null // Adapt this
        );
    }
}
