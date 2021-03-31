package ch.liip.timeforcoffee.api.mappers;

import android.graphics.Color;

import java.util.Date;

import ch.liip.timeforcoffee.api.models.Departure;

public class DepartureMapper {

    public static Departure fromBackend(ch.liip.timeforcoffee.backend.Departure backendDeparture) {
        int colorBg = Color.parseColor(backendDeparture.getColors().getBg());
        int colorFg = Color.parseColor(backendDeparture.getColors().getFg());

        Date departureScheduled = null;
        Date departureRealtime = null;
        Date arrivalScheduled = null;
        Date arrivalRealtime = null;

        if(backendDeparture.getDeparture() != null) {
            departureScheduled = backendDeparture.getDeparture().getScheduled();
            departureRealtime = backendDeparture.getDeparture().getRealtime();
        }
        if(backendDeparture.getArrival() != null) {
            arrivalScheduled = backendDeparture.getArrival().getScheduled();
            arrivalRealtime = backendDeparture.getArrival().getRealtime();
        }

        return new Departure(
                backendDeparture.getId(),
                backendDeparture.getTo(),
                departureScheduled,
                departureRealtime,
                arrivalScheduled,
                arrivalRealtime,
                backendDeparture.getName(),
                backendDeparture.getPlatform(),
                colorFg,
                colorBg,
                backendDeparture.isAccessible(),
                false
        );
    }
}
