package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import java.util.Date;

import ch.liip.timeforcoffee.api.models.Connection;

public class ConnectionMapper {

    public static Connection fromBackend(ch.liip.timeforcoffee.backend.Connection backendConnection) {
        Location stationLocation = new Location("reverseGeocoded");
        stationLocation.setLatitude(backendConnection.getStation().getLocation().getX());
        stationLocation.setLongitude(backendConnection.getStation().getLocation().getY());

        Date timeScheduled = null;
        Date timeRealtime = null;

        if(backendConnection.getTime() != null) {
            timeScheduled = backendConnection.getTime().getScheduled();
            timeRealtime = backendConnection.getTime().getRealtime();
        }

        return new Connection(
                backendConnection.getStation().getId(),
                backendConnection.getStation().getName(),
                stationLocation,
                timeScheduled,
                timeRealtime
        );
    }
}
