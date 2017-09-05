package ch.liip.timeforcoffee.api.events;

public class FetchLocationsEvent {

    private final String x;
    private final String y;

    public FetchLocationsEvent(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }
}
