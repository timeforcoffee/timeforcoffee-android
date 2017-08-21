package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.liip.timeforcoffee.api.Connection;
import ch.liip.timeforcoffee.opendata.Checkpoint;

public class ConnectionMapper {

    public static List<Connection> fromOpenData(ch.liip.timeforcoffee.opendata.Connection connection) {
        List<Checkpoint> passList = connection.getSections().get(0).getJourney().getPassList();
        List<Connection> connections = new ArrayList<>();

        for(Checkpoint checkpoint : passList) {
            int stationId = Integer.parseInt(checkpoint.getStation().getId());
            String stationName = checkpoint.getStation().getName();
            Location stationLocation = new Location("reverseGeocoded");
            stationLocation.setLatitude(checkpoint.getStation().getCoordinate().getX());
            stationLocation.setLongitude(checkpoint.getStation().getCoordinate().getY());

            connections.add(new Connection(stationId, stationName, stationLocation, checkpoint.getDeparture(), null, checkpoint.getArrival(), null));
        }

        return connections;
    }

    public static Connection fromZvv(ch.liip.timeforcoffee.zvv.CheckPoint checkPoint) {
        int checkPointId = Integer.parseInt(checkPoint.getId());
        Location checkPointLocation = new Location("reverseGeocoded");
        checkPointLocation.setLongitude(checkPoint.getLocation().getLng());
        checkPointLocation.setLatitude(checkPoint.getLocation().getLat());

        return new Connection(checkPointId, checkPoint.getName(), checkPointLocation, checkPoint.getDeparture().getScheduled(),
                checkPoint.getDeparture().getRealtime(), checkPoint.getArrival().getScheduled(), checkPoint.getArrival().getRealtime());
    }

}
