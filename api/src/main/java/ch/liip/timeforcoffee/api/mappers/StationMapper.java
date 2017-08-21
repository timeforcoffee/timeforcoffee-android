package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import android.support.annotation.Nullable;
import ch.liip.timeforcoffee.api.Station;

public class StationMapper {

    public static Station fromZvv(ch.liip.timeforcoffee.zvv.Station station) {
        int id = Integer.parseInt(station.getId());
        Location location = new Location("reverseGeocoded");
        location.setLongitude(station.getLocation().getLng());
        location.setLatitude(station.getLocation().getLat());

        return new Station(id, station.getName(), 0.0f, location, false);
    }

    @Nullable
    public static Station fromLocation(ch.liip.timeforcoffee.opendata.Location location) {
        int id = location.getId() == null ? 0 : Integer.parseInt(location.getId());
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
            newStation = new Station(id, location.getName(), location.getDistance(), loc, false);
        }

        return newStation;
    }
}
