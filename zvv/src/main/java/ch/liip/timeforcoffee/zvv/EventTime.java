package ch.liip.timeforcoffee.zvv;

import java.util.Date;

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
