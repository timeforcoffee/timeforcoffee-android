package ch.liip.timeforcoffee.opendata;

/**
 * Created by fsantschi on 08/03/15.
 */
public class Section {
    private Journey journey;
    private String walk;
    private Checkpoint departure;
    private Checkpoint arrival;
    public Section() {}

    public Journey getJourney() {
        return journey;
    }

    public String getWalk() {
        return walk;
    }

    public Checkpoint getDeparture() {
        return departure;
    }

    public Checkpoint getArrival() {
        return arrival;
    }
}
