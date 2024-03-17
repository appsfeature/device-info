package com.deviceinfo.model;

import android.hardware.Sensor;

import com.deviceinfo.application.Apps;

import java.util.HashMap;
import java.util.List;

public class DeviceInfoResult {

    private HashMap<String, String> batteryInfo;
    private HashMap<String, String> cpuInfo;
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

    public HashMap<String, String> getBatteryInfo() {
        return batteryInfo;
    }

    public DeviceInfoResult setBatteryInfo(HashMap<String, String> batteryInfo) {
        this.batteryInfo = batteryInfo;
        return this;
    }

    public HashMap<String, String> getCpuInfo() {
        return cpuInfo;
    }

    public DeviceInfoResult setCpuInfo(HashMap<String, String> cpuInfo) {
        this.cpuInfo = cpuInfo;
        return this;
    }

    public HashMap<String, String> getNetworkInfo() {
        return networkInfo;
    }

    public DeviceInfoResult setNetworkInfo(HashMap<String, String> networkInfo) {
        this.networkInfo = networkInfo;
        return this;
    }

    public HashMap<String, String> getDisplayInfo() {
        return displayInfo;
    }

    public DeviceInfoResult setDisplayInfo(HashMap<String, String> displayInfo) {
        this.displayInfo = displayInfo;
        return this;
    }

    public HashMap<String, String> getDeviceInfo() {
        return deviceInfo;
    }

    public DeviceInfoResult setDeviceInfo(HashMap<String, String> deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    public HashMap<String, String> getFeatureInfo() {
        return featureInfo;
    }

    public DeviceInfoResult setFeatureInfo(HashMap<String, String> featureInfo) {
        this.featureInfo = featureInfo;
        return this;
    }

    public List<Apps> getApplicationInfo() {
        return applicationInfo;
    }

    public DeviceInfoResult setApplicationInfo(List<Apps> applicationInfo) {
        this.applicationInfo = applicationInfo;
        return this;
    }

    public HashMap<String, String> getBluetoothInfo() {
        return bluetoothInfo;
    }

    public DeviceInfoResult setBluetoothInfo(HashMap<String, String> bluetoothInfo) {
        this.bluetoothInfo = bluetoothInfo;
        return this;
    }

    public HashMap<String, String> getDeviceConfigInfo() {
        return deviceConfigInfo;
    }

    public DeviceInfoResult setDeviceConfigInfo(HashMap<String, String> deviceConfigInfo) {
        this.deviceConfigInfo = deviceConfigInfo;
        return this;
    }

    public HashMap<String, String> getDeviceMemoryInfo() {
        return deviceMemoryInfo;
    }

    public DeviceInfoResult setDeviceMemoryInfo(HashMap<String, String> deviceMemoryInfo) {
        this.deviceMemoryInfo = deviceMemoryInfo;
        return this;
    }

    public List<Sensor> getSensorsInfo() {
        return sensorsInfo;
    }

    public DeviceInfoResult setSensorsInfo(List<Sensor> sensorsInfo) {
        this.sensorsInfo = sensorsInfo;
        return this;
    }

    public HashMap<String, String> getDeviceSimInfo() {
        return deviceSimInfo;
    }

    public DeviceInfoResult setDeviceSimInfo(HashMap<String, String> deviceSimInfo) {
        this.deviceSimInfo = deviceSimInfo;
        return this;
    }
}
