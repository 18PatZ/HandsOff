package com.patrickzhong.aplux;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patrickzhong on 3/17/17.
 */

public class Util {

    public static String id;
    public static String desc;
    public static boolean reportRequesting = false;
    public static LocationListener locationListener;
    public static Location lastLoc;

    public static void pushLocation(String id, String desc, Location location){
        DatabaseReference ref = ScrollingActivity.ref.child(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Description", desc);
        map.put("Location", serialize(location));
        ref.updateChildren(map);
        //ScrollingActivity.ref.child(id).child("Description").setValue(desc);
        //ScrollingActivity.ref.child(id).child("Location").setValue(serialize(location));
        ReportActivity.instance.showProgress(false);
        ReportActivity.instance.startActivity(new Intent(ReportActivity.instance, ScrollingActivity.class));
    }

    public static String serialize(Location location){
        return location.getLatitude() + " " + location.getLongitude() + " " + location.getAltitude();
    }

    public static void requestLoc(AppCompatActivity cont) {
        if (ContextCompat.checkSelfPermission(cont, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            setLoc(cont);
        else
            ActivityCompat.requestPermissions(cont, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }



    public static void preparePush(String idN, String descN){
        id = idN;
        desc = descN;
    }

    private static void setLoc(final AppCompatActivity cont){

        if(locationListener != null)
            return;

        final LocationManager locationManager = (LocationManager) cont.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                System.out.println("LOCATION RECIEVED");
                if(ScrollingActivity.instance.requesting) {
                    ScrollingActivity.handle(location);
                    ScrollingActivity.instance.requesting = false;
                }
                else if(reportRequesting) {
                    Util.pushLocation(id, desc, location);
                    reportRequesting = false;
                }

                lastLoc = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if(ContextCompat.checkSelfPermission(cont, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
