package ch.liip.timeforcoffee.backend;

public class Station {

    private String id;
    private String name;
    private Coordinate coordinate;
    private String distance;

    public Station() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String getDistance() {
        return distance;
    }
}
