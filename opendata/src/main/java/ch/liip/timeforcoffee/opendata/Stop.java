package ch.liip.timeforcoffee.opendata;

/**
 * Created by fsantschi on 09/03/15.
 */
public class Stop {
    private Location station;
    private String name;
    private String category;
    private String number;
    private String operator;
    private String to;

    public Stop() {}

    public Location getStation() {
        return station;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getNumber() {
        return number;
    }

    public String getOperator() {
        return operator;
    }

    public String getTo() {
        return to;
    }
}
