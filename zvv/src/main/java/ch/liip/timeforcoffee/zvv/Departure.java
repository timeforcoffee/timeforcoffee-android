package ch.liip.timeforcoffee.zvv;

public class Departure {

    private String id;
    private String name;
    private String type;
    private Colors colors;
    private String to;
    private String platform;
    private EventTime departure;
    private EventTime arrival;
    private Boolean accessible;

    public Departure() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Colors getColors() {
        return colors;
    }

    public String getTo() { return to; }

    public String getPlatform() { return platform; }

    public EventTime getDeparture() {
        return departure;
    }

    public EventTime getArrival() {
        return arrival;
    }

    public Boolean getAccessible()  {
        return accessible;
    }
}
