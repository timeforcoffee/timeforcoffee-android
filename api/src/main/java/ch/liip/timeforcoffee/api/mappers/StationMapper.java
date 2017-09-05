package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import android.support.annotation.Nullable;
import ch.liip.timeforcoffee.api.Station;

public class StationMapper {

    public static Station fromBackend(ch.liip.timeforcoffee.backend.Station station) {
        int id = Integer.parseInt(station.getId());
        float distance = Float.parseFloat(station.getDistance());
        Location location = new Location("reverseGeocoded");
        location.setLongitude(station.getCoordinate().getX());
        location.setLatitude(station.getCoordinate().getY());

        return new Station(id, station.getName(), distance, location, false);
    }
}
