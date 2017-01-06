package ch.liip.timeforcoffee.opendata;

/**
 * Created by fsantschi on 08/03/15.
 */
public class Location {
    private String id;
    private String type;
    private String name;
    private float score;
    private Coordinate coordinate;
    private float distance;
    Location() {}

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public float getScore() {
        return score;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public float getDistance() {
        return distance;
    }
}
