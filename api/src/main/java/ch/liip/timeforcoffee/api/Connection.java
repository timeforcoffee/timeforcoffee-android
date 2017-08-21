package ch.liip.timeforcoffee.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Connection {

    private String name;

    private String stationName;
    private Date scheduledArrival;
    private Date scheduledDeparture;

    public Connection(String stationName, Date scheduledArrival, Date scheduledDeparture) {
        this.stationName = stationName;
        this.scheduledArrival = scheduledArrival;
        this.scheduledDeparture = scheduledDeparture;
    }

    public String getStationName() {
        return stationName;
    }

    public String getTimeStr() {
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
