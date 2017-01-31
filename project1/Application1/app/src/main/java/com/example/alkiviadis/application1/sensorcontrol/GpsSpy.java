package com.example.alkiviadis.application1.sensorcontrol;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class GpsSpy implements LocationListener{
    public static Double latitude = null;
    public static Double longitude = null;

    @Override
    public void onLocationChanged(Location location) {

        if(location != null)
        {
            Log.e("Latitude:", "" + location.getLatitude());
            Log.e("Longtitude:", "" + location.getLongitude());

            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
