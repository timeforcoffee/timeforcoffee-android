package ch.liip.timeforcoffee.backend;

public class Departure {

    private int id;
    private String name;
    private String platform;
    private String to;
    private boolean accessible;
    private Color colors;
    private Time departure;
    private Time arrival;

    public Departure() {}

    public int getId() {
        return id;
    }

    public String getName() { return name; }

    public String getPlatform() {
        return platform;
    }

    public String getTo() { return to; }

    public boolean isAccessible() {
        return accessible;
    }

    public Color getColors() {
        return colors;
    }

    public Time getDeparture() {
        return departure;
    }

    public Time getArrival() {
        return arrival;
    }
}
