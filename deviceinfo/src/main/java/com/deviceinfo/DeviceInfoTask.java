package com.deviceinfo;

import android.content.Context;

import com.deviceinfo.application.Application;
import com.deviceinfo.battery.Battery;
import com.deviceinfo.config.DeviceConfig;
import com.deviceinfo.cpu.CPU;
import com.deviceinfo.device.UserDeviceInfo;
import com.deviceinfo.display.DeviceDisplay;
import com.deviceinfo.features.Feature;
import com.deviceinfo.interfaces.DeviceInfoCallback;
import com.deviceinfo.memory.DeviceMemory;
import com.deviceinfo.model.DeviceInfoResult;
import com.deviceinfo.permission.bluetooth.Bluetooth;
import com.deviceinfo.permission.network.Network;
import com.deviceinfo.permission.sim.DeviceSim;
import com.deviceinfo.sensors.Sensors;
import com.deviceinfo.util.DIResult;

public class DeviceInfoTask {

    private final DeviceInfoCallback<DeviceInfoResult> callback;
    private final DeviceInfo deviceInfo;

    public DeviceInfoTask(DeviceInfo deviceInfo, DeviceInfoCallback<DeviceInfoResult> callback) {
        this.callback = callback;
        this.deviceInfo = deviceInfo;
    }

    protected DIResult doInBackground(Context context) {

        try {
            //permission not required
            DeviceInfoResult jsonResponse = new DeviceInfoResult()
                    .setDeviceInfo(new UserDeviceInfo(context).getInfo(context))
                    .setBatteryInfo(new Battery().getInfo(context))
                    .setDisplayInfo(new DeviceDisplay(context).getInfo(context))
                    .setCpuInfo(new CPU().getInfo())
                    .setFeatureInfo(new Feature(context).getInfo(context))
                    .setDeviceConfigInfo(new DeviceConfig().getInfo(context))
                    .setDeviceMemoryInfo(new DeviceMemory().getInfo(context));

            if (deviceInfo.isEnableSensorInfo) {
                jsonResponse.setSensorsInfo(new Sensors(context).getInfo());
            }
            if (deviceInfo.isEnableApplicationInfo) {
                jsonResponse.setApplicationInfo(new Application().getInfo(context));
            }

            //permission required
            if(deviceInfo.isEnablePermissionRequiredInfo) {
                if (deviceInfo.isEnableBluetoothInfo) {
                    jsonResponse.setBluetoothInfo(new Bluetooth().getInfo(context));
                }
                if (deviceInfo.isEnableNetworkInfo) {
                    jsonResponse.setNetworkInfo(new Network(context).getInfo(context));
                }
                if (deviceInfo.isEnableSIMInfo) {
                    jsonResponse.setDeviceSimInfo(new DeviceSim(context).getInfo(context));
                }
            }

            return new DIResult(jsonResponse);
        } catch (Exception e) {
            return new DIResult(e);
        }
    }

    protected void onPostExecute(DIResult result) {
       if (result.getException() != null) {
            callback.onError(result.getException());
        } else {
            callback.onSuccess(result.getResult());
        }
    }

    public void execute(Context context){
        onPostExecute(doInBackground(context));
    }

}
