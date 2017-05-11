package ch.liip.timeforcoffee.api.events;

import java.util.LinkedList;
import java.util.List;

import ch.liip.timeforcoffee.opendata.Journey;
import ch.liip.timeforcoffee.opendata.Location;

/**
 * Created by fsantschi on 08/03/15.
 */
public class OpenDataStationboardFetchedEvent {
    private List<Journey> stationboards;

    public OpenDataStationboardFetchedEvent(List<Journey> journeys) {
        if(journeys != null) {
            this.stationboards = journeys;
        } else {
            this.stationboards = new LinkedList<>();
        }

    }

    public List<Journey> getStationboards() {
        return stationboards;
    }
}
