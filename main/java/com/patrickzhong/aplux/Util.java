package com.patrickzhong.aplux;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import static com.patrickzhong.aplux.ScrollingActivity.ref;

/**
 * Created by patrickzhong on 3/17/17.
 */

public class Util {

    public static void pushLocation(String id, String desc, Location location){
        ScrollingActivity.ref.child(id).child("Description").setValue(desc);
        ScrollingActivity.ref.child(id).child("Location").setValue(serialize(location));
    }

    public static String serialize(Location location){
        return location.getLatitude() + " " + location.getLongitude() + " " + location.getAltitude();
    }

    public static void addReport(AppCompatActivity cont){

        LocationManager locationManager = (LocationManager) cont.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                int r = (int) (Math.random() * 100);
                Util.pushLocation(""+r, "Filler description: "+r, location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if(ContextCompat.checkSelfPermission(cont, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public static void go(AppCompatActivity cont){
        if(ContextCompat.checkSelfPermission(cont, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            addReport(cont);
        else
            ActivityCompat.requestPermissions(cont, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 5);
    }

}
