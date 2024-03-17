
package com.deviceinfo.permission.sim;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.deviceinfo.util.DIValidityCheck;
import com.deviceinfo.util.EasyDeviceInfo;
import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.PermissionUtility;
import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * EasySim Mod Class
 */
public class DeviceSim {

    private final TelephonyManager tm;

    public DeviceSim(Context context) {
        this.tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public HashMap<String, String> getInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = new HashMap<>();
        try {
            info.put("carrier", getCarrier());
            info.put("country", getCountry());
            info.put("sim_network_locked", isSimNetworkLocked());
            //Permission Required

            List<SubscriptionInfo> simInfo = getActiveMultiSimInfo(context);
            info.put("IMEI", getIMEI(context));
            info.put("IMSI", getIMSI(context));
            info.put("number_of_active_sim", getNumberOfActiveSim(context));
            info.put("sim_serial", getSIMSerial(context));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                if(PermissionUtility.isPermissionGranted(context, permission.READ_PHONE_STATE)){
                    info.put("sim_subscription", getSimSubscription(simInfo));
                }else{
                    info.put("sim_subscription", "Permission Missing (READ_PHONE_STATE)");
                }
            }
        } catch (Exception e) {
            DILogger.e(e.toString());
            info.put("exception", e.toString());
        }

        DITimeLogger.timeLogging("Sim", startTime);
        return info;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private String getSimSubscription(List<SubscriptionInfo> sensorsInfo) {
        JSONArray apps = new JSONArray();
        for (SubscriptionInfo item : sensorsInfo) {
            try {
                JSONObject object = new JSONObject();
                object.put("carrier_name", item.getCarrierName());
                object.put("country_iso", item.getCountryIso());
                object.put("display_name", item.getDisplayName());
                object.put("icc_id", item.getIccId());
                object.put("mcc", item.getMcc());
                object.put("number", item.getNumber());
                object.put("sim_slot_index", item.getSimSlotIndex());
                object.put("subscription_id", item.getSubscriptionId());
                apps.put(object);
            } catch (JSONException e) {
                DILogger.e(e.toString());
            }
        }
        return apps.toString();
    }

    /**
     * Gets active multi sim info.
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return the active multi sim info
     */
//    @RequiresPermission(permission.READ_PHONE_STATE)
    public final List<SubscriptionInfo> getActiveMultiSimInfo(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && PermissionUtility.hasPermission(
                    context, permission.READ_PHONE_STATE)) {
                @SuppressLint("MissingPermission") List<SubscriptionInfo> tempActiveSub =
                        SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                if (tempActiveSub == null || tempActiveSub.isEmpty()) {
                    return new ArrayList<>(0);
                } else {
                    return tempActiveSub;
                }
            } else {
                if (EasyDeviceInfo.debuggable) {
                    DILogger.d(EasyDeviceInfo.nameOfLib,
                            "Device is running on android version that does not support multi sim functionality!");
                }
            }
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
        }
        return new ArrayList<>(0);
    }


    /**
     * Gets carrier.
     *
     * @return the carrier
     */
    public final String getCarrier() {
        try {
            String result = null;
            if ((tm != null) && (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA)) {
                result = this.tm.getNetworkOperatorName().toLowerCase(Locale.getDefault());
            }
            return DIValidityCheck.checkValidData(
                    DIValidityCheck.handleIllegalCharacterInResult(result));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Gets country.
     *
     * @return the country
     */
    public final String getCountry() {
        try {
            final String result;
            if ((tm != null) && (tm.getSimState() == TelephonyManager.SIM_STATE_READY)) {
                result = this.tm.getSimCountryIso().toLowerCase(Locale.getDefault());
            } else {
                final Locale locale = Locale.getDefault();
                result = locale.getCountry().toLowerCase(locale);
            }
            return DIValidityCheck.checkValidData(
                    DIValidityCheck.handleIllegalCharacterInResult(result));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Gets imsi.
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return the imsi
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
//    @RequiresPermission(permission.READ_PHONE_STATE)
    public final String getIMSI(Context context) {
        if (!PermissionUtility.hasPermission(context, permission.READ_PHONE_STATE)) {
            return "Permission Missing (READ_PHONE_STATE)";
        }
        try {
            String result = null;
            if ((tm != null) && PermissionUtility.hasPermission(context, permission.READ_PHONE_STATE)) {
                result = this.tm.getSubscriberId();
            }

            return DIValidityCheck.checkValidData(result);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    //    @RequiresPermission(permission.READ_PHONE_STATE)
    @SuppressLint("MissingPermission")
    public final String getIMEI(Context context) {
        if (!PermissionUtility.hasPermission(context, permission.READ_PHONE_STATE)) {
            return "Permission Missing (READ_PHONE_STATE)";
        }
        try {
            String result = null;
            if ((tm != null) && PermissionUtility.hasPermission(context, permission.READ_PHONE_STATE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    result = this.tm.getImei();
                }
            }

            return DIValidityCheck.checkValidData(result);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Gets number of active sim.
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return the number of active sim
     */
//    @RequiresPermission(permission.READ_PHONE_STATE)
    public final String getNumberOfActiveSim(Context context) {
        if (!PermissionUtility.hasPermission(context, permission.READ_PHONE_STATE)) {
            return "Permission Missing (READ_PHONE_STATE)";
        }
        try {
            return String.valueOf(getActiveMultiSimInfo(context).size());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Gets SIM serial number.
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return the sim serial
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
//    @RequiresPermission(permission.READ_PHONE_STATE)
    public final String getSIMSerial(Context context) {
        if (!PermissionUtility.hasPermission(context, permission.READ_PHONE_STATE)) {
            return "Permission Missing (READ_PHONE_STATE)";
        }
        try {
            String result = null;
            if ((tm != null) && PermissionUtility.hasPermission(context, permission.READ_PHONE_STATE)) {
                result = this.tm.getSimSerialNumber();
            }
            return DIValidityCheck.checkValidData(result);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Is multi sim.
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return the boolean
     */
//    @RequiresPermission(permission.READ_PHONE_STATE)
    public final boolean isMultiSim(Context context) {
        return getActiveMultiSimInfo(context).size() > 1;
    }

    /**
     * Is sim network locked.
     *
     * @return the boolean
     */
    public final String isSimNetworkLocked() {
        try {
            if ((tm != null) && (tm.getSimState() == TelephonyManager.SIM_STATE_NETWORK_LOCKED)) {
                return DIUtility.YES;
            } else {
                return DIUtility.NO;
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
