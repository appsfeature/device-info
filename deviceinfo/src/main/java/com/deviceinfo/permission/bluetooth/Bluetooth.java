package com.deviceinfo.permission.bluetooth;

import android.Manifest.permission;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;
import com.deviceinfo.util.PermissionUtility;

import java.util.HashMap;

/**
 * EasyBluetooth Mod Class
 */
public class Bluetooth {
 
    /**
     * Instantiates a new Easy bluetooth mod.
     */

    public Bluetooth() {
    }

    public HashMap<String, String> getInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = new HashMap<>();
        try {
            info.put("has_bluetooth_le", hasBluetoothLeDetail(context));
            //Permission Required
            info.put("has_bluetooth_le_advertising", hasBluetoothLeAdvertising(context));
        } catch (Exception e) {
            DILogger.e(e.toString());
            info.put("exception", e.toString());
        }
        DITimeLogger.timeLogging("Bluetooth", startTime);
        return info;
    }


    /**
     * Has Bluetooth LE
     *
     * @return true if the device has a Bluetooth LE compatible chip set
     */
    public final String hasBluetoothLeDetail(Context context) {
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            return "YES";
        }else {
            return "NO";
        }
    }

    public final Boolean hasBluetoothLe(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * Has Bluetooth LE advertising
     *
     * @return true if the device has Bluetooth LE advertising features
     */
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @RequiresPermission(permission.BLUETOOTH)
    public final String hasBluetoothLeAdvertising(Context context) {
        if (!PermissionUtility.hasPermission(context, permission.BLUETOOTH)) {
            return "Permission Missing (BLUETOOTH)";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (this.hasBluetoothLe(context) && BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported()) {
                return "YES";
            } else {
                return "NO";
            }
        } else {
            return DIUtility.UNKNOWN;
        }
    }
}
