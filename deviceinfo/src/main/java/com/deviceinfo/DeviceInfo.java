package com.deviceinfo;

import android.Manifest;
import android.content.Context;

import com.deviceinfo.interfaces.DeviceInfoCallback;
import com.deviceinfo.model.DeviceInfoResult;
import com.deviceinfo.util.PermissionUtility;
import com.deviceinfo.util.DIUtility;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfo {
    private DeviceInfoCallback<DeviceInfoResult> callback;

    boolean isEnableApplicationInfo = true;
    boolean isEnableSensorInfo = true;
    boolean isEnableNetworkInfo = true;
    boolean isEnableBluetoothInfo = true;
    boolean isEnableSIMInfo = true;

    public boolean isEnablePermissionRequiredInfo = false;
    public boolean isDebugMode = false;

    private DeviceInfo() {
    }

    public static DeviceInfo getInstance() {
        return new DeviceInfo();
    }

    /**
     * Add callback for getting response form this library.
     */
    public DeviceInfo addCallback(DeviceInfoCallback<DeviceInfoResult> callback) {
        this.callback = callback;
        return this;
    }
    /**
     * Generate and return in form of JSON of android device information.
     */
    public void fetch(final Context context) {
        if(callback == null){
            DIUtility.showToast(context, "Callback not registered.");
            return;
        }
        if(!isEnablePermissionRequiredInfo){
            executeTask(context);
        }else {
            checkPermission(context, new DeviceInfoCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    executeTask(context);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }


    private void checkPermission(Context context, final DeviceInfoCallback<Boolean> callback) {
        List<String> permission = new ArrayList<>();

        if (PermissionUtility.hasPermissionInManifest(context, Manifest.permission.READ_PHONE_STATE)) {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (PermissionUtility.hasPermissionInManifest(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (PermissionUtility.hasPermissionInManifest(context, Manifest.permission.BLUETOOTH)) {
            permission.add(Manifest.permission.BLUETOOTH);
        }

        if(permission.size()>0) {
            String[] permissionList = permission.toArray(new String[0]);
            TedPermission.with(context)
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            callback.onSuccess(true);
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            callback.onSuccess(true);
                        }
                    })
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(permissionList)
                    .check();
        }else{
            executeTask(context);
        }

    }

    private void executeTask(Context context) {
        new DeviceInfoTask(this, callback).execute(context);
    }

    /**
     * Add Permission based details in output JSON response.
     */
    public DeviceInfo setEnablePermissionRequiredInfo(Boolean isEnable) {
        this.isEnablePermissionRequiredInfo = isEnable;
        return this;
    }

    /**
     * Remove Application based details from output JSON response.
     */
    public DeviceInfo setEnableApplicationInfo(Boolean isEnable) {
        this.isEnableApplicationInfo = isEnable;
        return this;
    }

    /**
     * Remove Network based details from output JSON response.
     */
    public DeviceInfo setEnableNetworkInfo(Boolean isEnable) {
        this.isEnableNetworkInfo = isEnable;
        return this;
    }

    /**
     * Remove Bluetooth based details from output JSON response.
     */
    public DeviceInfo setEnableBluetoothInfo(Boolean isEnable) {
        this.isEnableBluetoothInfo = isEnable;
        return this;
    }

    /**
     * Remove SIM based details from output JSON response.
     */
    public DeviceInfo setEnableSIMInfo(Boolean isEnable) {
        this.isEnableSIMInfo = isEnable;
        return this;
    }

    /**
     * Remove Sensor based details from output JSON response.
     */
    public DeviceInfo setEnableSensorInfo(Boolean isEnable) {
        this.isEnableSensorInfo = isEnable;
        return this;
    }

    public DeviceInfo setDebugMode(Boolean isEnable) {
        this.isDebugMode = isEnable;
        return this;
    }
}