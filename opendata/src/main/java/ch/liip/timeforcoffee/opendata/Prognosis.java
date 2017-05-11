package ch.liip.timeforcoffee.opendata;

import java.util.Date;

/**
 * Created by fsantschi on 08/03/15.
 */
public class Prognosis {
    private String platform;
    private Date departure;
    private Date arrival;
    private Float capacity1st;
    private Float capacity2st;
    Prognosis() {}

    public String getPlatform() {
        return platform;
    }

    public Date getDeparture() {
        return departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public Float getCapacity1st() {
        return capacity1st;
    }

    public Float getCapacity2st() {
        return capacity2st;
    }
}
