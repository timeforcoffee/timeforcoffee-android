package ch.liip.timeforcoffee.zvv;

/**
 * Created by fsantschi on 09/03/15.
 */
public class Departure {
    private String zvv_id;
    private String name;
    private String type;
    private Colors colors;
    private String to;
    private String platform;
    private DepartureTime departure;
    private Boolean accessible;

    public Departure() {};

    public String getZvvId() {
        return zvv_id;
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

    public DepartureTime getDeparture() {
        return departure;
    }

    public Boolean getAccessible()  {
        return accessible;
    }
}
