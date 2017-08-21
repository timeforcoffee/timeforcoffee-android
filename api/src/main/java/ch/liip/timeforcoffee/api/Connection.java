package ch.liip.timeforcoffee.api;

import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Connection {

    private int id;
    private String name;
    private Location location;
    private Date scheduledDeparture;
    private Date realtimeDeparture;
    private Date scheduledArrival;
    private Date realTimeArrival;

    public Connection(int id, String name, Location location, Date scheduledDeparture, Date realtimeDeparture, Date scheduledArrival, Date realTimeArrival) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.scheduledDeparture = scheduledDeparture;
        this.realtimeDeparture = realtimeDeparture;
        this.scheduledArrival = scheduledArrival;
        this.realTimeArrival = realTimeArrival;
    }

    public String getName() {
        return name;
    }

    public String getTimeStr() { // handle realtime
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        if(scheduledArrival != null && scheduledDeparture != null) {
            String arrivalStr = timeFormatter.format(scheduledArrival);
            String departureStr = timeFormatter.format(scheduledDeparture);
            if(arrivalStr.equals(departureStr)) {
                return "Arr / Dep: " + arrivalStr;
            } else {
                return "Arr: " + arrivalStr + " / Dep: " + departureStr;
            }
        } else if (scheduledArrival == null) {
            return "Dep: " + timeFormatter.format(scheduledDeparture);
        }

        return "Arr: " + timeFormatter.format(scheduledArrival);
    }
}
