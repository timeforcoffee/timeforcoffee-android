package ch.liip.timeforcoffee.api.events.stationsLocationEvents;

public class FetchStationsLocationEvent {

    private double x;
    private double y;

    public FetchStationsLocationEvent(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
