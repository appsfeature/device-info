package com.deviceinfo;

import android.Manifest;
import android.content.Context;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import com.deviceinfo.interfaces.DeviceInfoCallback;
import com.deviceinfo.model.DeviceInfoResult;
import com.deviceinfo.util.DIResult;
import com.deviceinfo.util.PermissionUtility;
import com.deviceinfo.util.DIUtility;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceInfo {
    private DeviceInfoCallback<DeviceInfoResult> callback;
    private final List<String> permission = new ArrayList<>();

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
     * Generate and return in form of Model of android device information.
     */
    @MainThread
    public void fetch(final Context context) {
        if (callback == null) {
            DIUtility.showToast(context, "Callback not registered.");
            return;
        }
        if (!isEnablePermissionRequiredInfo) {
            executeTask(context);
        } else {
            checkPermission(context, new DeviceInfoCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    executeTask(context);
                }

                @Override
                public void onError(Exception e) {
                    callback.onError(e);
                }
            });
        }
    }

    @WorkerThread
    public DIResult fetchEnqueue(final Context context) {
        return new DeviceInfoTask().doInBackground(context);
    }

    @MainThread
    private void executeTask(Context context) {
        new DeviceInfoTask(callback).execute(context);
    }


    private void checkPermission(Context context, final DeviceInfoCallback<Boolean> callback) {
        if (!permission.contains(Manifest.permission.READ_PHONE_STATE)
                && PermissionUtility.hasPermissionInManifest(context, Manifest.permission.READ_PHONE_STATE)) {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!permission.contains(Manifest.permission.ACCESS_COARSE_LOCATION)
                && PermissionUtility.hasPermissionInManifest(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!permission.contains(Manifest.permission.ACCESS_FINE_LOCATION)
                && PermissionUtility.hasPermissionInManifest(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!permission.contains(Manifest.permission.BLUETOOTH)
                && PermissionUtility.hasPermissionInManifest(context, Manifest.permission.BLUETOOTH)) {
            permission.add(Manifest.permission.BLUETOOTH);
        }

        if (permission.size() > 0) {
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
        } else {
            executeTask(context);
        }

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

    /**
     * @param permissions Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION
     * @return this
     */
    public DeviceInfo setPermission(String... permissions) {
        this.permission.clear();
        this.permission.addAll(Arrays.asList(permissions));
        return this;
    }
}