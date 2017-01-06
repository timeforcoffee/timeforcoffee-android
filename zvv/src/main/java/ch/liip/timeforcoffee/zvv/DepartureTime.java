package ch.liip.timeforcoffee.zvv;

import java.util.Date;

/**
 * Created by fsantschi on 09/03/15.
 */
public class DepartureTime {
    private Date scheduled;
    private Date realtime;

    public DepartureTime() {}

    public Date getScheduled() {
        return scheduled;
    }

    public Date getRealtime() {
        return realtime;
    }
}
