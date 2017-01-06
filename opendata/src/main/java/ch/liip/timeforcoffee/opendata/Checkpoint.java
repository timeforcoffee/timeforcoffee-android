package ch.liip.timeforcoffee.opendata;

import java.util.Date;

/**
 * Created by fsantschi on 08/03/15.
 */
public class Checkpoint {
    private Location station;
    private Date arrival;
    private Date departure;
    private String platform;
    private Prognosis prognosis;
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
