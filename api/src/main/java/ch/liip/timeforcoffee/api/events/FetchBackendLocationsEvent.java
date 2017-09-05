package ch.liip.timeforcoffee.api.events;

public class FetchBackendLocationsEvent {

    private String x;
    private String y;

    public FetchBackendLocationsEvent(String x, String y) {
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
