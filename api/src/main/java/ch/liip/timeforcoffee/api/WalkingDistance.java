package ch.liip.timeforcoffee.api;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by nicolas on 29/04/16.
 */
public class WalkingDistance {

    private String walkingDistance;
    private PolylineOptions walkingPath;

    public  WalkingDistance(String walkingDistance, PolylineOptions walkingPath) {
        this.walkingDistance = walkingDistance;
        this.walkingPath = walkingPath;
    }

    public String getWalkingDistance() {
        return walkingDistance;
    }

    public void setWalkingDistance(String walkingDistance) {
        this.walkingDistance = walkingDistance;
    }

    public PolylineOptions getWalkingPath() {
        return walkingPath;
    }

    public void setWalkingPath(PolylineOptions walkingPath) {
        this.walkingPath = walkingPath;
    }
}
