package ch.liip.timeforcoffee.backend;

import androidx.annotation.Nullable;

public class Station {

    private int id;
    private String name;
    private Location coordinate;

    public Station() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCoordinate() {
        return coordinate;
    }
}
