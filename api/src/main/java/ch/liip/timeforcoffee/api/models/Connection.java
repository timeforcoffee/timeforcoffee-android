package ch.liip.timeforcoffee.api.models;

import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Connection {

    private int id;
    private String name;
    private Location location;
    private Date scheduledDeparture;
    private Date realtimeDeparture;
    private Date scheduledArrival;

    public Connection(int id, String name, Location location, Date scheduledDeparture, Date realtimeDeparture, Date scheduledArrival) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.scheduledDeparture = scheduledDeparture;
        this.realtimeDeparture = realtimeDeparture;
        this.scheduledArrival = scheduledArrival;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getTimeLabel(String departureLabel, String arrivalLabel) {
        if(scheduledArrival != null && scheduledDeparture != null) {
            if(scheduledArrival.getTime() == scheduledDeparture.getTime()) {
                return arrivalLabel;
            }
        }

        return departureLabel;
    }

    public String getScheduledDepartureStr() {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (scheduledDeparture != null) {
            return dt.format(scheduledDeparture);
        }

        return null;
    }

    public String getRealtimeDepartureStr() {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        if (realtimeDeparture != null) {
            return dt.format(realtimeDeparture);
        }

        return null;
    }

    public String getDepartureInMinutes() {
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
        Date now = new Date();
        long diff = -1;

        if (realtimeDeparture != null){
            diff = realtimeDeparture.getTime() - now.getTime();
        } else if (scheduledDeparture != null){
            diff = scheduledDeparture.getTime() - now.getTime();
        }

        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }

    public Boolean isLate() {
        return realtimeDeparture != null && scheduledDeparture != null && realtimeDeparture.compareTo(scheduledDeparture) != 0;
    }
}
