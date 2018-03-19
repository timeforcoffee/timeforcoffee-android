package ch.liip.timeforcoffee.api.mappers;

import android.graphics.Color;

import java.util.Date;

import ch.liip.timeforcoffee.api.models.Departure;

public class DepartureMapper {

    public static Departure fromZvv(ch.liip.timeforcoffee.zvv.Departure zvvDeparture, int stationId) {
        int id = zvvDeparture.getId() != null ? Integer.parseInt(zvvDeparture.getId()) : 0;
        int colorBg = Color.WHITE;
        int colorFg = Color.BLACK;

        try {
            colorBg = Color.parseColor(zvvDeparture.getColors().getBg());
            colorFg = Color.parseColor(zvvDeparture.getColors().getFg());
        }
        catch (Throwable ignored) { }

        Date departureScheduled = zvvDeparture.getDeparture() == null ? null : zvvDeparture.getDeparture().getScheduled();
        Date departureRealtime = zvvDeparture.getDeparture() == null ? null : zvvDeparture.getDeparture().getRealtime();
        Date arrivalScheduled = zvvDeparture.getArrival() == null ? null : zvvDeparture.getArrival().getScheduled();

        // If destination id and station id are equal, it is not a valid departure
        if(id == stationId) {
            return null;
        }

        return new Departure(zvvDeparture.getName(),
                id,
                zvvDeparture.getTo(),
                zvvDeparture.getPlatform(),
                colorFg,
                colorBg,
                departureScheduled,
                departureRealtime,
                arrivalScheduled,
                zvvDeparture.getAccessible(),
                false
        );
    }
}
