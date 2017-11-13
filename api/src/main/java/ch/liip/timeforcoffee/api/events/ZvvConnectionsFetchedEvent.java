package ch.liip.timeforcoffee.api.events;

import java.util.List;

import ch.liip.timeforcoffee.zvv.CheckPoint;

public class ZvvConnectionsFetchedEvent {

    private final List<CheckPoint> checkPoints;

    public ZvvConnectionsFetchedEvent(List<CheckPoint> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public List<CheckPoint> getCheckPoints() {
        return checkPoints;
    }

}
