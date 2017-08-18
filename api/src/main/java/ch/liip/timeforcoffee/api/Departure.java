package ch.liip.timeforcoffee.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Departure {

    private String name;
    private String lineNumber;
    private String type;
    private boolean accessible;
    private String destination;
    private String platform;
    private Date scheduled;
    private Date realtime;
    private int colorFg;
    private int colorBg;
    private boolean isFavorite;

    public Departure(String name, String lineNumber, String to, String platform, int colorFg, int colorBg, Date scheduled, Date realtime, boolean accessible, boolean isFavorite) {
        this.name = name;
        this.lineNumber = lineNumber;
        this.colorBg = colorBg;
        this.colorFg = colorFg;
        this.destination = to;
        this.platform = platform;
        this.scheduled = scheduled;
        this.realtime = realtime;
        this.accessible = accessible;
        this.isFavorite = isFavorite;
    }

    public Departure(String lineNumber, String destination, boolean isFavorite) {
        this.lineNumber = lineNumber;
        this.destination = destination;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public String getType() {
        return type;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public String getDestination() {
        return destination;
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

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean lineEquals(Object object) {
        boolean sameLineNumber = false;
        boolean sameDestination = false;

        if (object != null && object.getClass() == Departure.class) {
            sameLineNumber = this.lineNumber.equals(((Departure) object).getLineNumber());
            sameDestination = this.destination.equals(((Departure) object).getDestination());
        }

        return sameLineNumber && sameDestination;
    }
}
