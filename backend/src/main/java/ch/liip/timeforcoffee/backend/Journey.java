package ch.liip.timeforcoffee.backend;

public class Journey {

    private String name;
    private String destination_id;
    private String destination_name;
    private Time departure;
    private Time arrival;
    private Color color;

    public String getName() {
        return name;
    }

    public String getDestination_id() {
        return destination_id;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public Time getDeparture() {
        return departure;
    }

    public Time getArrival() {
        return arrival;
    }

    public Color getColor() {
        return color;
    }
}
