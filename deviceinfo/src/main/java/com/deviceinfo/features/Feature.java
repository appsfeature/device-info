package com.deviceinfo.features;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;

import com.deviceinfo.extras.DeviceNFC;
import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.DITimeLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Feature {
    private final DeviceNFC deviceNfc;

    public Feature(Context context) {
        deviceNfc = new DeviceNFC(context);
    }

    public HashMap<String, String> getInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = new HashMap<>();
        info.put("nfc", deviceNfc.nfcEnabled());
        info.put("connected_devices_list", getConnectedDevicesList(context));
        info.put("multi_touch", checkMultiTouchSupport(context));
        DITimeLogger.timeLogging("Feature", startTime);
        return info;
    }

    public String checkMultiTouchSupport(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) {
            return "Supported";
        } else {
            return "Not Supported";
        }
    }

    public String getConnectedDevicesList(Context context) {
        try {
            JSONArray jsonArray = new JSONArray();
            UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
            for (UsbDevice device : deviceList.values()) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("device_id", device.getDeviceId());
                    object.put("device_name", device.getDeviceName());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        object.put("manufacturer_name", device.getManufacturerName());
                        object.put("product_name", device.getProductName());
                        object.put("serial_number", device.getSerialNumber());
                    }
                    object.put("product_id", device.getProductId());
                    object.put("vendor_id", device.getVendorId());
                    jsonArray.put(object);
                } catch (JSONException e) {
                    DILogger.e(e.toString());
                }
            }
            if(jsonArray.length()>0) {
                return jsonArray.toString();
            }else{
                return "No device connected";
            }
        }catch (Exception e){
            return e.getMessage();
        }

    }


}
