package ch.liip.timeforcoffee.backend;

public class Connection {

    private int id;
    private String name;
    private Coordinate coordinate;
    private Time departure;

    public Connection() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Time getDeparture() {
        return departure;
    }
}
