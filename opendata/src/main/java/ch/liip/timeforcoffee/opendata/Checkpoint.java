package ch.liip.timeforcoffee.opendata;

import java.util.Date;

/**
 * Created by fsantschi on 08/03/15.
 */
public class Checkpoint {
    private Location station;
    private Date arrival;
    private Integer arrivalTimestamp;
    private Date departure;
    private Integer departureTimestamp;
    private Integer delay;
    private String platform;
    private Prognosis prognosis;
    private Object realtimeAvailability;
    private Location location;

    Checkpoint() {}

    public Location getStation() {
        return station;
    }

    public Date getArrival() {
        return arrival;
    }

    public Date getDeparture() {
        return departure;
    }

    public String getPlatform() {
        return platform;
    }

    public Prognosis getPrognosis() {
        return prognosis;
    }
}
