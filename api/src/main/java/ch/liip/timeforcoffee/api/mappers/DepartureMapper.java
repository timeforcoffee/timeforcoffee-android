package ch.liip.timeforcoffee.api.mappers;

import android.graphics.Color;

import ch.liip.timeforcoffee.api.Departure;

/**
 * Created by fsantschi on 13/03/15.
 */
public class DepartureMapper {
    public static Departure fromZvv(ch.liip.timeforcoffee.zvv.Departure zvvDeparture) {

        int colorBg = Color.WHITE;
        int colorFg = Color.BLACK;
        try {
            colorBg = Color.parseColor(zvvDeparture.getColors().getBg());
            colorFg = Color.parseColor(zvvDeparture.getColors().getFg());
        } catch (Throwable e) {
            //
        }
        return new Departure(zvvDeparture.getName(), zvvDeparture.getTo(), zvvDeparture.getPlatform(), colorFg, colorBg, zvvDeparture.getDeparture().getScheduled(), zvvDeparture.getDeparture().getRealtime(), zvvDeparture.getAccessible(), false);
    }
}
