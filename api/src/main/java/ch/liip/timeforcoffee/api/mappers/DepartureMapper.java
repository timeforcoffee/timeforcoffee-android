package ch.liip.timeforcoffee.api.mappers;

import android.graphics.Color;

import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.backend.Journey;

public class DepartureMapper {

    public static Departure fromBackend(Journey journey) {
        int destinationId = Integer.parseInt(journey.getDestination_id());

        int colorBg = Color.WHITE;
        int colorFg = Color.BLACK;
        try {
            colorBg = Color.parseColor(journey.getColor().getFg());
            colorFg = Color.parseColor(journey.getColor().getBg());
        } catch (Throwable e) { }

        return new Departure(
                journey.getName(),
                destinationId,
                journey.getDestination_name(),
                "testPlatform" /* HANDLE */,
                colorFg,
                colorBg,
                journey.getDeparture().getScheduled(),
                journey.getDeparture().getRealtime(),
                journey.getArrival().getScheduled(),
                true /* HANDLE */,
                false
        );
    }
}
