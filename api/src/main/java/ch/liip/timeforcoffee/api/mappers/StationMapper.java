package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import android.support.annotation.Nullable;
import ch.liip.timeforcoffee.api.Station;

/**
 * Created by fsantschi on 11/03/15.
 */
public class StationMapper {
    public static Station fromZvv(ch.liip.timeforcoffee.zvv.Station station) {
        Location location = new Location("reverseGeocoded");
        location.setLongitude(station.getLocation().getLng());
        location.setLatitude(station.getLocation().getLat());
        return new Station(station.getId(), station.getName(), 0.0f, location, false);
    }

    @Nullable
    public static Station fromLocation(ch.liip.timeforcoffee.opendata.Location location) {
        Location loc = new Location("reverseGeocoded");
        Station newStation = null;
        // Sanity check.
        if (
                location.getCoordinate() != null
                && location.getId() != null
                && location.getCoordinate().getX() != null
                && location.getCoordinate().getY() != null
        ) {
            loc.setLatitude(location.getCoordinate().getX());
            loc.setLongitude(location.getCoordinate().getY());
            newStation = new Station(location.getId(), location.getName(), location.getDistance(), loc, false);
        }

        return newStation;
    }
}
