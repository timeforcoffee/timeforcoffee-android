package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import java.util.Date;

import ch.liip.timeforcoffee.api.models.Connection;

public class ConnectionMapper {

    public static Connection fromZvv(ch.liip.timeforcoffee.zvv.CheckPoint checkPoint) {
        int checkPointId = Integer.parseInt(checkPoint.getId());
        Location checkPointLocation = new Location("reverseGeocoded");
        checkPointLocation.setLongitude(checkPoint.getLocation().getLng());
        checkPointLocation.setLatitude(checkPoint.getLocation().getLat());

        Date departureScheduled = checkPoint.getDeparture().getScheduled();
        Date departureRealtime = checkPoint.getDeparture().getRealtime();
        Date arrivalScheduled = checkPoint.getArrival().getScheduled();

        // If there is no specified departure and arrival, it is not a valid connection
        if(arrivalScheduled == null && departureScheduled == null) {
            return null;
        }

        return new Connection(checkPointId, checkPoint.getName(), checkPointLocation, departureScheduled, departureRealtime, arrivalScheduled);
    }
}
