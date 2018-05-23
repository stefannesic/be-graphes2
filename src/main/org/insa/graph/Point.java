package org.insa.graph;

/**
 * Class representing a point (position) on Earth.
 *
 */
public final class Point {

    /**
     * Approximated Earth radius (in meters).
     */
    public static final double EARTH_RADIUS = 6378137.0;

    /**
     * Compute the distance in meters between the two given points.
     * 
     * @param p1 First point.
     * @param p2 second point.
     * 
     * @return Distance between the two given points (in meters).
     */
    public static double distance(Point p1, Point p2) {
        double sinLat = Math.sin(Math.toRadians(p1.getLatitude()))
                * Math.sin(Math.toRadians(p2.getLatitude()));
        double cosLat = Math.cos(Math.toRadians(p1.getLatitude()))
                * Math.cos(Math.toRadians(p2.getLatitude()));
        double cosLong = Math.cos(Math.toRadians(p2.getLongitude() - p1.getLongitude()));
        return EARTH_RADIUS * Math.acos(sinLat + cosLat * cosLong);
    }

    // Longitude and latitude of the point.
    private final float longitude, latitude;

    /**
     * Create a new point corresponding to the given (longitude, latitude) position.
     * 
     * @param longitude Longitude of the point (in degrees).
     * @param latitude Latitude of the point (in degrees).
     */
    public Point(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    /**
     * Calculate midpoint between two points.
     */
    public static Point midPoint(Point p1, Point p2) {
    	Point result;
    	//Bx = cos(lat2) * cos(lon2-lon1);
    	//By = cos(lat2) * sin(lon2-lon1);
    	double Bx = Math.cos(Math.toRadians(p2.getLatitude())) * Math.cos(Math.toRadians(p2.getLongitude()) - Math.toRadians(p1.getLongitude()));
    	double By = Math.cos(Math.toRadians(p2.getLatitude())) * Math.sin(Math.toRadians(p2.getLongitude()) - Math.toRadians(p1.getLongitude()));
    	/*latMid = atan2(sin(lat1) + sin(lat2), ...
               sqrt( (cos(lat1)+Bx)*(cos(lat1)+Bx) + By*By ) );
		lonMid = lon1 + atan2(By, cos(lat1) + Bx);*/
    	float latMid;
    	float lonMid;
    	latMid = (float)Math.atan2(Math.sin(Math.toRadians(p1.getLatitude() + Math.toRadians(p2.getLatitude()))), Math.sqrt((Math.cos(Math.toRadians(p1.getLatitude()))+Bx)*(Math.cos(Math.toRadians(p1.getLatitude()))+Bx) + By*By ));
    	lonMid = p1.getLongitude() + (float)Math.atan2(By, (Math.cos(Math.toRadians(p1.getLatitude())) + Bx));
    	result = new Point(lonMid, latMid);
    	return result;
    }

    /**
     * @return Longitude of this point (in degrees).
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * @return Latitude of this point (in degrees).
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Compute the distance from this point to the given point
     * 
     * @param target Target point to compute distance to.
     * 
     * @return Distance between this point and the target point, in meters.
     */
    public double distanceTo(Point target) {
        return distance(this, target);
    }

    @Override
    public String toString() {
        return String.format("Point(%f, %f)", getLongitude(), getLatitude());
    }
}
