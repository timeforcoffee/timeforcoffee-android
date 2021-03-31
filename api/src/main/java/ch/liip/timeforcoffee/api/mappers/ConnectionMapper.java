package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import java.util.Date;

import ch.liip.timeforcoffee.api.models.Connection;

public class ConnectionMapper {

    public static Connection fromBackend(ch.liip.timeforcoffee.backend.Connection backendConnection) {
        Location stationLocation = new Location("reverseGeocoded");
        stationLocation.setLatitude(backendConnection.getLocation().getLatitude());
        stationLocation.setLongitude(backendConnection.getLocation().getLongitude());

        Date departureTimeScheduled = null;
        Date departureTimeRealtime = null;
        Date arrivalTimeScheduled = null;
        Date arrivalTimeRealtime = null;

        if(backendConnection.getDeparture() != null) {
            departureTimeScheduled = backendConnection.getDeparture().getScheduled();
            departureTimeRealtime = backendConnection.getDeparture().getRealtime();
        }

        if(backendConnection.getArrival() != null) {
            arrivalTimeScheduled = backendConnection.getArrival().getScheduled();
            arrivalTimeRealtime = backendConnection.getArrival().getRealtime();
        }

        return new Connection(
                backendConnection.getId(),
                backendConnection.getName(),
                stationLocation,
                departureTimeScheduled,
                departureTimeRealtime,
                arrivalTimeScheduled,
                arrivalTimeRealtime
        );
    }
}
