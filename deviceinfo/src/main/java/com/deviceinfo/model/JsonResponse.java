package com.deviceinfo.model;

import android.hardware.Sensor;

import com.deviceinfo.application.Apps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResponse {

    private final JSONObject mainObject;
    private HashMap<String, String> batteryInfo;
    private JSONObject cpuInfo;
    private HashMap<String, String> networkInfo;
    private HashMap<String, String> displayInfo;
    private HashMap<String, String> deviceInfo;
    private HashMap<String, String> featureInfo;
    private List<Apps> applicationInfo;
    private HashMap<String, String> bluetoothInfo;
    private HashMap<String, String> deviceConfigInfo;
    private HashMap<String, String> deviceMemoryInfo;
    private List<Sensor> sensorsInfo;
    private HashMap<String, String> deviceSimInfo;

    public JsonResponse() {
        mainObject = new JSONObject();
    }

    public static JsonResponse Builder() {
        return new JsonResponse();
    }


    public JSONObject getJsonResponse() throws JSONException {
        if (deviceInfo != null)
            addDetailInJsonObject("device", getJsonObject(deviceInfo));
        if (batteryInfo != null)
            addDetailInJsonObject("battery", getJsonObject(batteryInfo));
        if (cpuInfo != null)
            addDetailInJsonObject("cpu", cpuInfo);
        if (networkInfo != null)
            addDetailInJsonObject("network", getJsonObject(networkInfo));
        if (displayInfo != null)
            addDetailInJsonObject("display", getJsonObject(displayInfo));
        if (bluetoothInfo != null)
            addDetailInJsonObject("bluetooth", getJsonObject(bluetoothInfo));
        if (deviceConfigInfo != null)
            addDetailInJsonObject("device_settings", getJsonObject(deviceConfigInfo));
        if (deviceMemoryInfo != null)
            addDetailInJsonObject("memory", getJsonObject(deviceMemoryInfo));
        if (deviceSimInfo != null)
            addDetailInJsonObject("SIM", getJsonObject(deviceSimInfo));
        if (featureInfo != null)
            addDetailInJsonObject("features", getJsonObject(featureInfo));
        if (sensorsInfo != null)
            addDetailInJsonArray("sensors", getJsonArrayOfSensors(sensorsInfo));
        if (applicationInfo != null)
            addDetailInJsonArray("application_info", getJsonArrayOfApps(applicationInfo));
        return mainObject;
    }

    private JSONArray getJsonArrayOfApps(List<Apps> applicationInfo) throws JSONException {
        JSONArray apps = new JSONArray();
        for (Apps item : applicationInfo) {
            JSONObject object = new JSONObject();
            object.put("app_installed_name", item.getName());
            object.put("icon", item.getIcon());
            object.put("package_name", item.getPackageName());
            object.put("version", item.getVersion());
            object.put("target_sdk_version", item.getTargetSdkVersion());
            object.put("app_installed_date", item.getInstalledDate());
            object.put("app_installed_date_formatted", item.getApp_installed_date_formatted());
            object.put("app_last_modified_date_formatted", item.getApp_last_modified_date_formatted());
            object.put("last_modified", item.getLastModified());
            object.put("manifest_launched_activity", item.getLaunchActivity());
            object.put("manifest_req_permission", item.getReqPermission());
            object.put("manifest_path_info", item.getProviders());
            object.put("manifest_services", item.getServices());
            object.put("manifest_activities", item.getActivities());
            apps.put(object);
        }
        return apps;
    }

    private JSONArray getJsonArrayOfSensors(List<Sensor> sensorsInfo) throws JSONException {
        JSONArray apps = new JSONArray();
        for (Sensor item : sensorsInfo) {
            JSONObject object = new JSONObject();
            object.put("name", item.getName());
            object.put("vendor", item.getVendor());
            object.put("version", item.getVersion());
            apps.put(object);
        }
        return apps;
    }

    private void addDetailInJsonString(String key, String string) throws JSONException {
        if (string != null) {
            mainObject.put(key, string);
        }
    }

    private void addDetailInJsonObject(String key, JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            mainObject.put(key, jsonObject);
        }
    }

    private void addDetailInJsonArray(String key, JSONArray jsonArray) throws JSONException {
        if (jsonArray != null) {
            mainObject.put(key, jsonArray);
        }
    }


    private JSONObject getJsonObject(HashMap<String, String> list) throws JSONException {
        if (list == null) {
            return null;
        }
        JSONObject battery = new JSONObject();
        for (Map.Entry<String, String> entry : list.entrySet()) {
            battery.put(entry.getKey(), entry.getValue());
        }
        return battery;
    }

    public JsonResponse setBatteryInfo(HashMap<String, String> batteryInfo) {
        this.batteryInfo = batteryInfo;
        return this;
    }

    public JsonResponse setCPUInfo(JSONObject cpuInfo) {
        this.cpuInfo = cpuInfo;
        return this;
    }

    public JsonResponse setNetwork(HashMap<String, String> networkInfo) {
        this.networkInfo = networkInfo;
        return this;
    }


    public JsonResponse setDisplay(HashMap<String, String> displayInfo) {
        this.displayInfo = displayInfo;
        return this;
    }

    public JsonResponse setDeviceInfo(HashMap<String, String> deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    public JsonResponse setFeatures(HashMap<String, String> featureInfo) {
        this.featureInfo = featureInfo;
        return this;
    }

    public JsonResponse setApplication(List<Apps> applicationInfo) {
        this.applicationInfo = applicationInfo;
        return this;
    }

    public JsonResponse setBluetooth(HashMap<String, String> bluetoothInfo) {
        this.bluetoothInfo = bluetoothInfo;
        return this;
    }

    public JsonResponse setDeviceConfig(HashMap<String, String> deviceConfigInfo) {
        this.deviceConfigInfo = deviceConfigInfo;
        return this;
    }

    public JsonResponse setDeviceMemory(HashMap<String, String> deviceMemoryInfo) {
        this.deviceMemoryInfo = deviceMemoryInfo;
        return this;
    }

    public JsonResponse setSensors(List<Sensor> sensorsInfo) {
        this.sensorsInfo = sensorsInfo;
        return this;
    }

    public JsonResponse setDeviceSim(HashMap<String, String> deviceSimInfo) {
        this.deviceSimInfo = deviceSimInfo;
        return this;
    }
}
