package com.deviceinfo.util;

import java.util.concurrent.TimeUnit;

public class DITimeLogger {

    private static final String TAG = "TimeLogger_DeviceInfo";

    public static long getStartTime() {
        return System.currentTimeMillis();
    }
    public static void timeLogging(String name, long netStartTime) {
        long netStopTime = System.currentTimeMillis();
        long netDiffInMill = netStopTime - netStartTime;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(netDiffInMill);
        DILogger.d(TAG, name +" takes time: " + seconds + "sec");
    }
}
