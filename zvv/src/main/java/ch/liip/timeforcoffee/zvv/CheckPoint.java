package ch.liip.timeforcoffee.zvv;

public class CheckPoint {

    private String id;
    private String name;
    private Location location;
    private EventTime departure;
    private EventTime arrival;

    public CheckPoint() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public EventTime getDeparture() {
        return departure;
    }

    public EventTime getArrival() {
        return arrival;
    }
}
