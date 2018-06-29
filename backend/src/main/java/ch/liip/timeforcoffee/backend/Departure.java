package ch.liip.timeforcoffee.backend;

public class Departure {

    private Station destination;
    private String line;
    private String platform;
    private boolean accessible;
    private Color color;
    private Time departure;
    private Time arrival;

    public Departure() {}

    public Station getDestination() {
        return destination;
    }

    public String getLine() {
        return line;
    }

    public String getPlatform() {
        return platform;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public Color getColor() {
        return color;
    }

    public Time getDeparture() {
        return departure;
    }

    public Time getArrival() {
        return arrival;
    }
}
