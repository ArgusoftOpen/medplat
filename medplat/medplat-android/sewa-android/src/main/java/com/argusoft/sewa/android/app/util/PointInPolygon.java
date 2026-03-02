package com.argusoft.sewa.android.app.util;

import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.service.GPSTracker;

import java.util.List;
import java.util.Map;

public class PointInPolygon {

    private PointInPolygon() {
        throw new IllegalStateException();
    }

    private static final double PI = 3.14159265;
    private static final double TWO_PI = 2 * PI;

    public static boolean coordinateInsidePolygon() {
        SharedStructureData.gps.getLocation();
        double latitude = GPSTracker.latitude;
        double longitude = GPSTracker.longitude;

        if (latitude == 0 || longitude == 0) {
            return true;
        }

        if (SharedStructureData.techoService == null) {
            return true;
        }

        if (!SharedStructureData.techoService.checkIfFeatureIsReleased()) {
            return true;
        }


        if (SharedStructureData.mapOfLatLongWithLGDCode == null || SharedStructureData.mapOfLatLongWithLGDCode.isEmpty()) {
            return true;
        }
        boolean result = false;
        for (Map.Entry<String, Map<String, List<Double>>> mapEntry : SharedStructureData.mapOfLatLongWithLGDCode.entrySet()) {
            List<Double> latArray = mapEntry.getValue().get("latArray");
            List<Double> longArray = mapEntry.getValue().get("longArray");
            int i;
            double angle = 0;
            double point1Lat;
            Double point1Long;
            double point2Lat;
            Double point2Long;
            int n = latArray != null ? latArray.size() : 0;

            if (longArray != null) {
                for (i = 0; i < n; i++) {
                    //you should have paid more attention in high school geometry.
                    point1Lat = latArray.get(i) - latitude;
                    point2Lat = latArray.get((i + 1) % n) - latitude;
                    point1Long = longArray.get(i);
                    point2Long = longArray.get((i + 1) % n);
                    if (point1Long != null && point2Long != null) {
                        angle += angle2D(point1Lat, point1Long - longitude, point2Lat, point2Long - longitude);
                    }
                }
            }

            if (Math.abs(angle) < PI) {
                result = false;
            } else {
                result = true;
                break;
            }
        }
        return result;
    }

    public static double angle2D(double y1, double x1, double y2, double x2) {
        double dtheta;
        double theta1;
        double theta2;
        theta1 = Math.atan2(y1, x1);
        theta2 = Math.atan2(y2, x2);
        dtheta = theta2 - theta1;
        while (dtheta > PI)
            dtheta -= TWO_PI;
        while (dtheta < -PI)
            dtheta += TWO_PI;

        return (dtheta);
    }

    public static boolean isValidGpsCoordinate(double latitude, double longitude) {
        return latitude > -90 && latitude < 90 &&
                longitude > -180 && longitude < 180;
    }
}