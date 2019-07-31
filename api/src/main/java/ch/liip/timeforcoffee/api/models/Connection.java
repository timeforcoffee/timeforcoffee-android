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
    private Date scheduledTime;
    private Date realtimeTime;


    public Connection(int stationId, String stationName, Location stationLocation, Date scheduledTime, Date realtimeTime) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationLocation = stationLocation;
        this.scheduledTime = scheduledTime;
        this.realtimeTime = realtimeTime;
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

    public Boolean isLate() {
        return scheduledTime != null && realtimeTime != null && realtimeTime.compareTo(scheduledTime) != 0;
    }

    public @Nullable
    String getScheduledDepartureStr() {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (scheduledTime != null) {
            return dt.format(scheduledTime);
        }

        return null;
    }

    public @Nullable String getRealtimeDepartureStr() {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (realtimeTime != null) {
            return dt.format(realtimeTime);
        }

        return null;
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

        if (realtimeTime != null){
            diff = realtimeTime.getTime() - now.getTime();
        }
        else if (scheduledTime != null){
            diff = scheduledTime.getTime() - now.getTime();
        }

        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }


}
