package ch.liip.timeforcoffee.zvv;

import java.util.Date;

/**
 * Created by fsantschi on 09/03/15.
 */
public class EventTime {
    private Date scheduled;
    private Date realtime;

    public EventTime() {}

    public Date getScheduled() {
        return scheduled;
    }

    public Date getRealtime() {
        return realtime;
    }
}
