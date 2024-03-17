
package com.deviceinfo.extras;

import android.Manifest.permission;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import androidx.annotation.RequiresPermission;

import com.deviceinfo.util.PermissionUtility;

/**
 * EasyLocation Mod Class
 *
 * You need to declare the below permission in the manifest file to use this properly
 *
 * For Network based location
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 *
 * For more accurate location updates via GPS and network both
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 */

public class DeviceLocation {

    private final boolean hasCoarseLocationPermission;

    private final boolean hasFineLocationPermission;

    private LocationManager lm = null;

    /**
     * Instantiates a new Easy location mod.
     * @param context the context
     */
    public DeviceLocation(final Context context) {
        this.hasFineLocationPermission =
                PermissionUtility.hasPermission(context, permission.ACCESS_FINE_LOCATION);
        this.hasCoarseLocationPermission =
                PermissionUtility.hasPermission(context, permission.ACCESS_COARSE_LOCATION);

        if (this.hasCoarseLocationPermission || this.hasFineLocationPermission) {
            this.lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    /**
     * Get lat long double [ ].
     *
     * @return the double [ ]
     */
    @RequiresPermission(anyOf = {
            permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION
    })
    public final double[] getLatLong() {
        final double[] gps = new double[2];
        gps[0] = 0;
        gps[1] = 0;

        if (this.hasCoarseLocationPermission && this.lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            final Location lastKnownLocation = this.lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation != null) {
                gps[0] = lastKnownLocation.getLatitude();
                gps[1] = lastKnownLocation.getLongitude();
            }
        } else if (this.hasFineLocationPermission) {
            final boolean isGPSEnabled = this.lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            final boolean isNetworkEnabled = this.lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Location lastKnownLocationNetwork = null;
            Location lastKnownLocationGps = null;
            Location betterLastKnownLocation = null;

            if (isNetworkEnabled) {
                lastKnownLocationNetwork = this.lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                lastKnownLocationGps = this.lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if ((lastKnownLocationGps != null) && (lastKnownLocationNetwork != null)) {
                betterLastKnownLocation = this.getBetterLocation(lastKnownLocationGps, lastKnownLocationNetwork);
            }

            if (betterLastKnownLocation == null) {
                betterLastKnownLocation = this.lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }

            if (betterLastKnownLocation != null) {
                gps[0] = betterLastKnownLocation.getLatitude();
                gps[1] = betterLastKnownLocation.getLongitude();
            }
        }
        return gps;
    }

    private Location getBetterLocation(Location location1, Location location2) {
        return location1.getAccuracy() >= location2.getAccuracy() ? location1 : location2;
    }
}
