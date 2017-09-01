package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.liip.timeforcoffee.api.Connection;
import ch.liip.timeforcoffee.opendata.Checkpoint;

public class ConnectionMapper {

    public static Connection fromZvv(ch.liip.timeforcoffee.zvv.CheckPoint checkPoint) {
        int checkPointId = Integer.parseInt(checkPoint.getId());
        Location checkPointLocation = new Location("reverseGeocoded");
        checkPointLocation.setLongitude(checkPoint.getLocation().getLng());
        checkPointLocation.setLatitude(checkPoint.getLocation().getLat());

        Date departureScheduled = checkPoint.getDeparture() == null ? null : checkPoint.getDeparture().getScheduled();
        Date departureRealtime = checkPoint.getDeparture() == null ? null : checkPoint.getDeparture().getRealtime();
        Date arrivalScheduled = checkPoint.getArrival() == null ? null : checkPoint.getArrival().getScheduled();

        return new Connection(checkPointId, checkPoint.getName(), checkPointLocation, departureScheduled, departureRealtime, arrivalScheduled);
    }
}
