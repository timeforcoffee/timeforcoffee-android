package ch.liip.timeforcoffee.api;

import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by fsantschi on 08/03/15.
 */
public class Departure {
    private String name;
    private String type;
    private Boolean accessible;
    private String to;
    private String platform;
    private Date scheduled;
    private Date realtime;
    private int colorFg;
    private int colorBg;

    public Departure(String name, String to, String platform, int colorFg, int colorBg, Date scheduled, Date realtime, Boolean accessible) {
        this.name = name;
        this.colorBg = colorBg;
        this.colorFg = colorFg;
        this.to = to;
        this.platform = platform;
        this.scheduled = scheduled;
        this.realtime = realtime;
        this.accessible = accessible;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Boolean getAccessible() {
        return accessible;
    }

    public String getTo() {
        return to;
    }

    public String getPlatform() {return platform;}

    public Date getScheduled() {
        return scheduled;
    }

    public Date getRealtime() {
        return realtime;
    }

    public int getColorFg() {
        return colorFg;
    }

    public int getColorBg() {
        return colorBg;
    }

    public String getScheduledStr() {

        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (scheduled != null) {
            return dt.format(scheduled);
        }
        return null;
    }

    public String getRealtimeStr() {

        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (realtime != null) {
            return dt.format(realtime);
        }
        return null;
    }

    public Boolean hasRealtime(){
        return realtime != null;
    }

    //realtime != schedule time
    public Boolean isLate() {
        if (realtime != null && scheduled != null && realtime.compareTo(scheduled) != 0) {
            return true;
        }
        return false;
    }

    public Boolean isAccessible(){
        if (accessible != null){
            return getAccessible();
        }
        return false;
    }
    public String departureInMinutes() {

        long timeInterval = getTimeDiffInMinutes();

        if (timeInterval < 0){
            return "0'";
        }
        if (timeInterval >= 60){
            return ">59'";
        }
        return timeInterval +"'";
    }

    long getTimeDiffInMinutes() {
        long diff = -1;
        Date now = new Date();
        if (realtime != null){
            diff = realtime.getTime() - now.getTime();
        }else if (scheduled != null){
            diff = scheduled.getTime() - now.getTime();
        }
        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }
}
