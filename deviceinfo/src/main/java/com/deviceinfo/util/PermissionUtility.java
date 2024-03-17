
package com.deviceinfo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Permission Util Class
 */
public final class PermissionUtility {


    public static boolean hasPermission(Context context, String permission) {
        final boolean permGranted =
                context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        if (EasyDeviceInfo.debuggable && !permGranted) {
            DILogger.e(EasyDeviceInfo.nameOfLib, ">\t" + permission);
            DILogger.d(EasyDeviceInfo.nameOfLib,
                    "\nPermission not granted/missing!\nMake sure you have declared the permission in your manifest file (and granted it on API 23+).\n");
        }
        return permGranted;
    }

    public static boolean hasPermissionInManifest(Context context, String permission) {
        List<String> permissionList = getAllPermissions(context);
        Set<String> set = new HashSet<>(permissionList);
        return set.contains(permission);
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static List<String> getGrantedPermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        List<String> granted = new ArrayList<>();
        try {
            String appPackageName = context.getPackageName();
            PackageInfo pi = pm.getPackageInfo(appPackageName, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i]);
                }
            }
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
        }
        return granted;
    }

    public static List<String> getAllPermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        List<String> granted = new ArrayList<>();
        try {
            String appPackageName = context.getPackageName();
            PackageInfo pi = pm.getPackageInfo(appPackageName, PackageManager.GET_PERMISSIONS);
            granted.addAll(Arrays.asList(pi.requestedPermissions));
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
        }
        return granted;
    }


}
