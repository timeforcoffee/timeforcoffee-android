package ch.liip.timeforcoffee.opendata;

public class Location {

    private String id;
    private String name;
    private Coordinate coordinate;
    private Float distance;
    Location() {}

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Float getDistance() {
        return distance;
    }
}