package carbooking.utils;

import net.sf.geographiclib.*;


/**
 * Created by Daniel Handloser on 26.03.2020.
 */
public class Location {
    private double longitude;
    private double latitude;

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public double calculateDistance(Location locationToCompare) {
        GeodesicData g = Geodesic.WGS84.Inverse(latitude, longitude, locationToCompare.getLatitude(), locationToCompare.getLatitude());
        return g.s12;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
