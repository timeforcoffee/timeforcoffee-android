package ch.liip.timeforcoffee.api.models;

import android.location.Location;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Connection {

    private int stationId;
    private String stationName;
    private Location stationLocation;
    private Date departureScheduledTime;
    private Date departureRealtimeTime;
    private Date arrivalScheduledTime;
    private Date arrivalRealtimeTime;

    public Connection(int stationId, String stationName, Location stationLocation, Date departureScheduledTime, Date departureRealtimeTime, Date arrivalScheduledTime, Date arrivalRealtimeTime) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationLocation = stationLocation;
        this.departureScheduledTime = departureScheduledTime;
        this.departureRealtimeTime = departureRealtimeTime;
        this.arrivalScheduledTime = arrivalScheduledTime;
        this.arrivalRealtimeTime = arrivalRealtimeTime;
    }

    public int getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public Location getStationLocation() {
        return stationLocation;
    }

    public Boolean isDepartureLate() {
        return departureScheduledTime != null && departureRealtimeTime != null && departureRealtimeTime.compareTo(departureScheduledTime) != 0;
    }

    public Boolean isArrivalLate() {
        return arrivalScheduledTime != null && arrivalRealtimeTime != null && arrivalRealtimeTime.compareTo(arrivalScheduledTime) != 0;
    }

    public @Nullable
    String getScheduledDepartureStr() {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (departureScheduledTime != null) {
            return dt.format(departureScheduledTime);
        }

        return null;
    }

    public @Nullable String getRealtimeDepartureStr() {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (departureRealtimeTime != null) {
            return dt.format(departureRealtimeTime);
        }

        return null;
    }

    public @Nullable
    String getScheduledArrivalStr() {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (arrivalScheduledTime != null) {
            return dt.format(arrivalScheduledTime);
        }

        return null;
    }

    public @Nullable String getRealtimeArrivalStr() {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (arrivalRealtimeTime != null) {
            return dt.format(arrivalRealtimeTime);
        }

        return null;
    }

    public @Nullable String getArrivalInMinutes() {
        long timeInterval = getArrivalTimeDiffInMinutes();
        if (timeInterval < 0){
            return "0'";
        }
        if (timeInterval >= 60){
            return "> 59'";
        }

        return timeInterval +"'";
    }

    private long getArrivalTimeDiffInMinutes() {
        Date now = new Date();
        long diff = -1;

        if (arrivalRealtimeTime != null){
            diff = arrivalRealtimeTime.getTime() - now.getTime();
        }
        else if (arrivalScheduledTime != null){
            diff = arrivalScheduledTime.getTime() - now.getTime();
        }

        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }

    public @Nullable String getDepartureInMinutes() {
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
        Date now = new Date();
        long diff = -1;

        if (departureRealtimeTime != null){
            diff = departureRealtimeTime.getTime() - now.getTime();
        }
        else if (departureScheduledTime != null){
            diff = departureScheduledTime.getTime() - now.getTime();
        }

        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }
}
