package ch.liip.timeforcoffee.common;

/**
 * Created by nicolas on 31/01/17.
 */
public class SerializableLocation {

    private double mLatitude;
    private double mLongitude;

    public SerializableLocation(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }


}
