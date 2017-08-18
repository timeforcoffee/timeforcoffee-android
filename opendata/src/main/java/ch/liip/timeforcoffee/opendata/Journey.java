package ch.liip.timeforcoffee.opendata;

import java.util.List;

/**
 * Created by fsantschi on 08/03/15.
 */
public class Journey {
    private Checkpoint stop;
    private String name;
    private String category;
    private String subcategory;
    private String categoryCode;
    private String number;
    private String operator;
    private String to;
    private List<Checkpoint> passList;
    private String capacity1st;
    private String capacity2st;

    Journey() {}

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryCode() {
        return categoryCode;
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

    public List<Checkpoint> getPassList() {
        return passList;
    }

    public String getCapacity1st() {
        return capacity1st;
    }

    public String getCapacity2st() {
        return capacity2st;
    }

    public Checkpoint getStop() {
        return stop;
    }
}