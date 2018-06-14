package ch.liip.timeforcoffee.api.mappers;

import android.location.Location;

import ch.liip.timeforcoffee.api.models.Station;

public class StationMapper {

    public static Station fromBackend(ch.liip.timeforcoffee.backend.Station backendStation) {
        Location location = new Location("reverseGeocoded");
        location.setLongitude(backendStation.getLocation().getX());
        location.setLatitude(backendStation.getLocation().getY());

        return new Station(
                backendStation.getId(),
                backendStation.getName(),
                0.0f,
                location,
                false
        );
    }
}
