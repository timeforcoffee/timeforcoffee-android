package ch.liip.timeforcoffee.api.models;

import android.location.Location;

import com.directions.route.Route;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.PolylineOptions;

public class Station implements RoutingListener {

    private int id;
    private String name;
    private Location location;
    private float distance;
    private boolean isFavorite;

    public Station(int id, String name, float distance, Location location, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.distance = distance;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public float getDistance() {
        return distance;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getDistanceForDisplay(Location userLocation) {

        if (userLocation != null) {
            int directDistance = getDistanceInMeter(userLocation);

            if (directDistance > 5000) {
                int km = (int) (Math.round((double) (directDistance) / 1000));
                return km + " km";
            } else {
                return directDistance + " m";
            }
        }
        return null;
    }

    private int getDistanceInMeter(Location userLocation) {
        if (userLocation != null) {
            return (int) userLocation.distanceTo(location);
        }
        return 0;
    }

    @Override
    public void onRoutingFailure() {
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public boolean equals(Object object) {
        boolean sameName = false;

        if (object != null && object instanceof Station) {
            sameName = this.name.equals(((Station) object).getName());
        }

        return sameName;
    }
}
