package com.deviceinfo.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.deviceinfo.util.DIValidityCheck;
import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;

import java.util.HashMap;


public class Battery {

    public Battery() {
    }

    public HashMap<String, String> getInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = new HashMap<>();
        try {
            info.put("technology",getBatteryTechnology(context));
            info.put("temperature",String.valueOf(getBatteryTemperature(context)));
            info.put("voltage",String.valueOf(getBatteryVoltage(context)));
            info.put("charging_state",getDeviceChargingState(context));
            info.put("charging_source",getChargingSource(context));
            info.put("charging_source_index",getChargingSourceIndexDetail(context));
            info.put("is_battery_present", String.valueOf(isBatteryPresent(context)));
            info.put("charged_percentage", String.valueOf(getBatteryPercentage(context)));
            info.put("health",getBatteryHealth(context));
        } catch (Exception e) {
            DILogger.e(e.toString());
            info.put("exception", e.toString());
        }
        DITimeLogger.timeLogging("Battery", startTime);
        return info;
    }

    public final String getBatteryHealth(Context context) {
        try {
            String status = "Having Issues";
            int health;// = BatteryHealth.HAVING_ISSUES;
            final Intent batteryStatus = this.getBatteryStatusIntent(context);
            if (batteryStatus != null) {
                health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
//            health = health == BatteryManager.BATTERY_HEALTH_GOOD ? BatteryHealth.GOOD : BatteryHealth.HAVING_ISSUES;
                if(health == BatteryManager.BATTERY_HEALTH_GOOD){
                    status = "Battery health Good";
                }
            }
            return status;
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets battery percentage.
     *
     * @return the battery percentage
     */
    public final int getBatteryPercentage(Context context) {
        int percentage = 0;
        try {
            final Intent batteryStatus = this.getBatteryStatusIntent(context);
            if (batteryStatus != null) {
                final int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                final int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                percentage = (int) ((level / (float) scale) * 100);
            }
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
        }

        return percentage;
    }

    /**
     * Gets battery technology.
     *
     * @return the battery technology
     */
    public final String getBatteryTechnology(Context context) {
        try {
            return DIValidityCheck.checkValidData(
                    this.getBatteryStatusIntent(context).getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets battery temperature.
     *
     * @return the battery temperature
     */
    public final float getBatteryTemperature(Context context) {
        float temp = 0.0f;
        try {
            final Intent batteryStatus = this.getBatteryStatusIntent(context);
            if (batteryStatus != null) {
                temp = (float) (batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10.0);
            }
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
        }
        return temp;
    }

    /**
     * Gets battery voltage.
     *
     * @return the battery voltage
     */
    public final int getBatteryVoltage(Context context) {
        int volt = 0;
        try {
            final Intent batteryStatus = this.getBatteryStatusIntent(context);
            if (batteryStatus != null) {
                volt = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            }
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
        }

        return volt;
    }

    /**
     * Gets charging source.
     *
     * @return the charging source
     */
    public final String getChargingSource(Context context) {
        try {
            final Intent batteryStatus = this.getBatteryStatusIntent(context);
            final int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

            switch (chargePlug) {
                case BatteryManager.BATTERY_PLUGGED_AC:
                    return "Source-AC";
                case BatteryManager.BATTERY_PLUGGED_USB:
                    return "Source-USB";
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    return "Source-WIRELESS";
                default:
                    return "Source-UNKNOWN_SOURCE";
            }
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
            return e.getMessage();
        }
    }

    public final String getChargingSourceIndexDetail(Context context) {
        switch (getChargingSourceIndex(context)){
            case ChargingVia.USB:
                return "USB";
            case ChargingVia.AC:
                return "AC";
            case ChargingVia.WIRELESS:
                return "WIRELESS";
            case ChargingVia.UNKNOWN_SOURCE:
            default:
                return "UNKNOWN_SOURCE";
        }
    }
    public final int getChargingSourceIndex(Context context) {
        try {
            final Intent batteryStatus = this.getBatteryStatusIntent(context);
            final int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

            switch (chargePlug) {
                case BatteryManager.BATTERY_PLUGGED_AC:
                    return ChargingVia.AC;
                case BatteryManager.BATTERY_PLUGGED_USB:
                    return ChargingVia.USB;
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    return ChargingVia.WIRELESS;
                default:
                    return ChargingVia.UNKNOWN_SOURCE;
            }
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
            return ChargingVia.UNKNOWN_SOURCE;
        }
    }

    /**
     * Is battery present boolean.
     */
    public final boolean isBatteryPresent(Context context) {
        try {
            return (getBatteryStatusIntent(context).getExtras() != null) && this.getBatteryStatusIntent(context).getExtras()
                    .getBoolean(BatteryManager.EXTRA_PRESENT);
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
            return false;
        }
    }

    /**
     * Is device charging boolean.
     *
     * @return is battery charging boolean
     */
    public final String getDeviceChargingState(Context context) {
        if(isDeviceCharging(context)){
            return "Charging";
        }else {
            return "Charger not connected";
        }
    }

    public final boolean isDeviceCharging(Context context) {
        try {
            final Intent batteryStatus = this.getBatteryStatusIntent(context);
            final int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            return (status == BatteryManager.BATTERY_STATUS_CHARGING)
                    || (status == BatteryManager.BATTERY_STATUS_FULL);
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
            return false;
        }

    }

    public Intent getBatteryStatusIntent(Context context) throws Exception{
        final IntentFilter batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        return context.registerReceiver(null, batFilter);
    }
}