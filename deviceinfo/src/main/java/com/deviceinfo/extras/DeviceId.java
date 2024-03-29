
package com.deviceinfo.extras;

import android.Manifest.permission;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import androidx.annotation.RequiresPermission;
import android.webkit.WebSettings;

import com.deviceinfo.util.DIValidityCheck;
import com.deviceinfo.util.EasyDeviceInfo;
import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.PermissionUtility;

import java.util.UUID;

/**
 * EasyId Mod Class
 */
public class DeviceId {

    public DeviceId() {
    }

    /**
     * Get google email accounts
     *
     * You need to declare the below permission in the manifest file to use this properly
     *
     * <uses-permission android:name="android.permission.GET_ACCOUNTS"/>
     *
     * @return the string [ ]
     * @deprecated
     */
    @RequiresPermission(permission.GET_ACCOUNTS)
    @Deprecated
    public final String[] getAccounts(Context context) {
        String[] result = null;
        if ((VERSION.SDK_INT < Build.VERSION_CODES.O) && PermissionUtility.hasPermission(context,
                permission.GET_ACCOUNTS)) {
            final Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
            result = new String[accounts.length];
            for (int i = 0; i < accounts.length; i++) {
                result[i] = accounts[i].name;
            }
        }
        return DIValidityCheck.checkValidData(result);
    }

    /**
     * Gets android id.
     *
     * @return the android id
     * @deprecated
     */
    @SuppressLint("HardwareIds")
    @Deprecated
    public final String getAndroidID(Context context) {
        return DIValidityCheck.checkValidData(
                Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
    }

    /**
     * Returns the GSFID.
     *
     * You need to declare the below permission in the manifest file to use this properly
     *
     * <uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>
     *
     * @return the gsfid
     */
    @RequiresPermission("com.google.android.providers.gsf.permission.READ_GSERVICES")
    public final String getGSFID(Context context) {
        Uri uri = Uri.parse("content://com.google.android.gsf.gservices");
        String idKey = "android_id";

        final String[] params = {idKey};
        final Cursor c = context.getContentResolver().query(uri, null, null, params, null);

        if (c == null) {
            return EasyDeviceInfo.NOT_FOUND_VAL;
        } else if (!c.moveToFirst() || (c.getColumnCount() < 2)) {
            c.close();
            return EasyDeviceInfo.NOT_FOUND_VAL;
        }

        try {
            final String gsfID = Long.toHexString(Long.parseLong(c.getString(1)));
            c.close();
            return gsfID;
        } catch (final NumberFormatException e) {
            c.close();
            return EasyDeviceInfo.NOT_FOUND_VAL;
        }
    }

    /**
     * Gets psuedo unique id.
     *
     * @return the psuedo unique id
     */
    @SuppressWarnings("deprecation")
    public final String getPseudoUniqueID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String devIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10);

        devIDShort += VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? Build.SUPPORTED_ABIS[0].length() % 10
                : Build.CPU_ABI.length() % 10;

        devIDShort +=
                (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length()
                        % 10) + (Build.PRODUCT.length() % 10);

        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a duplicate entry
        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
        } catch (final Exception e) {
            // String needs to be initialized
            if (EasyDeviceInfo.debuggable) {
                DILogger.e(EasyDeviceInfo.nameOfLib, "getPseudoUniqueID: ", e);
            }
            serial = "ESYDV000"; // some value
        }

        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * Gets ua.
     *
     * @return the ua
     */
    public final String getUA(Context context) {
        String systemUa = System.getProperty("http.agent");
        final String result;
        result = WebSettings.getDefaultUserAgent(context)
                + "__" + systemUa;
        return DIValidityCheck.checkValidData(result);
    }
}
