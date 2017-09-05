package ch.liip.timeforcoffee.backend;

import java.util.Date;

public class Time {

    private Date scheduled;
    private Date realtime;

    public Time() {}

    public Date getScheduled() {
        return scheduled;
    }

    public Date getRealtime() {
        return realtime;
    }
}
