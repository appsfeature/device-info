package com.deviceinfo.util;

import android.util.Log;

import com.deviceinfo.DeviceInfo;


/**
 * Created by Abhijit on 7/1/2017.
 */

public class DILogger {

    private static final String TAG = "device-info";

    public static void e(String s) {
        if (DeviceInfo.getInstance().isDebugMode) {
            Log.d(TAG, s);
        }
    }

    public static void e(String q, String s) {
        if (DeviceInfo.getInstance().isDebugMode) {
            Log.d(TAG, q + " : " + s);
        }
    }

    public static void e(String q, String s, Exception e) {
        if (DeviceInfo.getInstance().isDebugMode) {
            Log.d(TAG, q + " : " + s + " exception:" + e.getMessage());
        }
    }

    public static void d(String q, String s) {
        if (DeviceInfo.getInstance().isDebugMode) {
            Log.d(TAG, q + " : " + s);
        }
    }

    public static void d(String q, String s, Exception e) {
        if (DeviceInfo.getInstance().isDebugMode) {
            Log.d(TAG, q + " : " + s + " exception:" + e.getMessage());
        }
    }


}
