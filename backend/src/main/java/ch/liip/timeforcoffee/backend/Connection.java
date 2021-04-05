package ch.liip.timeforcoffee.backend;

public class Connection {

    private int id;
    private String name;
    private ConnectionLocation location;
    private Time departure;
    private Time arrival;

    public Connection() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ConnectionLocation getLocation() {
        return location;
    }

    public Time getDeparture() { return departure; }

    public Time getArrival() { return arrival; }
}
