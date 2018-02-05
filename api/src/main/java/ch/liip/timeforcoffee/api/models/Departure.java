package ch.liip.timeforcoffee.api.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Departure {

    private String name;
    private int destinationId;
    private String destinationName;
    private String platform;
    private Date departureScheduled;
    private Date departureRealtime;
    private Date arrivalScheduled;
    private int colorFg;
    private int colorBg;
    private boolean accessible;
    private boolean isFavorite;

    public Departure(String name, int destinationId, String destinationName, String platform, int colorFg, int colorBg, Date departureScheduled, Date departureRealtime, Date arrivalScheduled, boolean accessible, boolean isFavorite) {
        this.name = name;
        this.colorBg = colorBg;
        this.colorFg = colorFg;
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.platform = platform;
        this.departureScheduled = departureScheduled;
        this.departureRealtime = departureRealtime;
        this.arrivalScheduled = arrivalScheduled;
        this.accessible = accessible;
        this.isFavorite = isFavorite;
    }

    public Departure(String name, int destinationId, boolean isFavorite) {
        this.name = name;
        this.destinationId = destinationId;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
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
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return dateFormatter.format(departureScheduled);
    }

    public String getArrivalStrForZvv() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return dateFormatter.format(arrivalScheduled);
    }

    //realtime != schedule time
    public Boolean isLate() {
        if (departureRealtime != null && departureScheduled != null && departureRealtime.compareTo(departureScheduled) != 0) {
            return true;
        }
        return false;
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

    long getDepartureTimeDiffInMinutes() {
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
            sameName = this.name.equals(((Departure) object).getName());
            sameDestinationId = this.destinationId == ((Departure) object).getDestinationId();
        }

        return sameName && sameDestinationId;
    }
}
