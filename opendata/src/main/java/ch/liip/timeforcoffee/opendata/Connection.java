package ch.liip.timeforcoffee.opendata;

import java.util.List;

/**
 * Created by fsantschi on 08/03/15.
 */
public class Connection {
    private Checkpoint from;
    private Checkpoint to;
    private String duration;
    private Service service;
    private List<String> products;
    private float capacity1st;
    private float capacity2st;
    private List<Section> sections;
    Connection() {}

    public Checkpoint getFrom() {
        return from;
    }

    public Checkpoint getTo() {
        return to;
    }

    public String getDuration() {
        return duration;
    }

    public Service getService() {
        return service;
    }

    public List<String> getProducts() {
        return products;
    }

    public float getCapacity1st() {
        return capacity1st;
    }

    public float getCapacity2st() {
        return capacity2st;
    }

    public List<Section> getSections() {
        return sections;
    }
}
