package ch.liip.timeforcoffee.api.mappers;

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
            String stationName = checkpoint.getStation().getName();
            Date scheduledArrival = checkpoint.getArrival();
            Date scheduledDeparture = checkpoint.getDeparture();

            connections.add(new Connection(stationName, scheduledArrival, scheduledDeparture));
        }

        return connections;
    }

}
