package ch.liip.timeforcoffee.api.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Departure {

    private int destinationId;
    private String destinationName;
    private Date departureScheduled;
    private Date departureRealtime;
    private Date arrivalScheduled;
    private Date arrivalRealtime;
    private String line;
    private String platform;
    private int colorFg;
    private int colorBg;
    private boolean accessible;
    private boolean isFavorite;

    public Departure(int destinationId, String destinationName, Date departureScheduled, Date departureRealtime, Date arrivalScheduled, Date arrivalRealtime, String line, String platform, int colorFg, int colorBg, boolean accessible, boolean isFavorite) {
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.departureScheduled = departureScheduled;
        this.departureRealtime = departureRealtime;
        this.arrivalScheduled = arrivalScheduled;
        this.arrivalRealtime = arrivalRealtime;
        this.line = line;
        this.platform = platform;
        this.colorBg = colorBg;
        this.colorFg = colorFg;
        this.accessible = accessible;
        this.isFavorite = isFavorite;
    }

    public Departure(int destinationId, String line, boolean isFavorite) {
        this.destinationId = destinationId;
        this.line = line;
        this.isFavorite = isFavorite;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public String getDestinationIdStr() {
        return String.valueOf(destinationId);
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getLine() {
        return line;
    }

    public String getPlatform() {return platform;}

    public Date getDepartureScheduled() {
        return departureScheduled;
    }

    public Date getDepartureRealtime() {
        return departureRealtime;
    }

    public Date getArrivalScheduled() {
        return arrivalScheduled;
    }

    public Date getArrivalRealtime() {
        return arrivalRealtime;
    }

    public int getColorFg() {
        return colorFg;
    }

    public int getColorBg() {
        return colorBg;
    }

    public String getDepartureScheduledStr() {

        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (departureScheduled != null) {
            return dt.format(departureScheduled);
        }
        return null;
    }

    public String getDepartureRealtimeStr() {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (departureRealtime != null) {
            return dt.format(departureRealtime);
        }

        return null;
    }

    public String getDepartureStrForZvv() {
        DateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        if (departureScheduled != null) {
            return dt.format(departureScheduled);
        }

        return null;
    }

    public String getArrivalStrForZvv() {
        DateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        if (arrivalScheduled != null) {
            return dt.format(arrivalScheduled);
        }

        return null;
    }

    public Boolean isLate() {
        return departureRealtime != null && departureScheduled != null && departureRealtime.compareTo(departureScheduled) != 0;
    }

    public String departureInMinutes() {
        long timeInterval = getDepartureTimeDiffInMinutes();
        if (timeInterval < 0){
            return "0'";
        }
        if (timeInterval >= 60){
            return "> 59'";
        }

        return timeInterval +"'";
    }

    private long getDepartureTimeDiffInMinutes() {
        long diff = -1;
        Date now = new Date();
        if (departureRealtime != null){
            diff = departureRealtime.getTime() - now.getTime();
        }else if (departureScheduled != null){
            diff = departureScheduled.getTime() - now.getTime();
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
        boolean sameName = false;
        boolean sameDestinationId = false;

        if (object != null && object.getClass() == Departure.class) {
            sameName = this.line.equals(((Departure) object).getLine());
            sameDestinationId = this.destinationId == ((Departure) object).getDestinationId();
        }

        return sameName && sameDestinationId;
    }
}
