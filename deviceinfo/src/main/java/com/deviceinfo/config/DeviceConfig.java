package com.deviceinfo.config;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;

import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class DeviceConfig {

    /**
     * Instantiates a new Easy config mod.
     */
    public DeviceConfig() {
    }

    public HashMap<String, String> getInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = new HashMap<>();
        try {
            info.put("current_date",getCurrentDate());
            info.put("device_ringer_mode", getDeviceRingerMode(context));
            info.put("formatted_date", getFormattedDate());
            info.put("formatted_time", getFormattedTime());
            info.put("formatted_up_time", getFormattedUpTime());
            info.put("sd_card_available", hasSdCard());
            info.put("running_on_emulator", isRunningOnEmulator());
        } catch (Exception e) {
            DILogger.e(e.toString());
            info.put("exception", e.toString());
        }
        DITimeLogger.timeLogging("Config", startTime);
        return info;
    }

    /**
     * Gets date from milliseconds
     */
    public final String getCurrentDate() {
        return DIUtility.formattedDate(System.currentTimeMillis());
    }

    /**
     * Gets Device Ringer Mode.
     */
    @RingerMode
    public final String getDeviceRingerMode(Context context) {
        String ringerMode = RingerMode.NORMAL;
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            switch (audioManager.getRingerMode()) {
                case RINGER_MODE_NORMAL:
                    ringerMode = RingerMode.NORMAL;
                    break;
                case RINGER_MODE_SILENT:
                    ringerMode = RingerMode.SILENT;
                    break;
                case RINGER_MODE_VIBRATE:
                    ringerMode = RingerMode.VIBRATE;
                    break;
                default:
                    //do nothing
                    break;
            }
        }

        return ringerMode;
    }

    /**
     * Gets formatted date.
     */
    public final String getFormattedDate() {
        final DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        return dateInstance.format(Calendar.getInstance().getTime());
    }

    /**
     * Gets formatted time.
     */
    public final String getFormattedTime() {
        final DateFormat timeInstance = SimpleDateFormat.getTimeInstance();
        return timeInstance.format(Calendar.getInstance().getTime());
    }

    /**
     * Gets formatted up time.
     */
    public final String getFormattedUpTime() {
        final DateFormat timeInstance = SimpleDateFormat.getTimeInstance();
        return timeInstance.format(Long.valueOf(SystemClock.uptimeMillis()));
    }

    /**
     * Gets time.
     */
    public final long getTime() {
        return System.currentTimeMillis();
    }

    /**
     * Gets up time.
     */
    public final long getUpTime() {
        return SystemClock.uptimeMillis();
    }

    /**
     * Checks if the device has sd card
     */
    public final String hasSdCard() {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return DIUtility.YES;
        }else {
            return DIUtility.NO;
        }
    }

    public final boolean hasSdCard(Context context) {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Is running on emulator boolean.
     */
    public final String isRunningOnEmulator() {
        final boolean isGenyMotion = Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("vbox86p")
                || Build.DEVICE.contains("vbox86p")
                || Build.HARDWARE.contains("vbox86");
        final boolean isGenericEmulator = Build.BRAND.contains("generic")
                || Build.DEVICE.contains("generic")
                || Build.PRODUCT.contains("sdk")
                || Build.HARDWARE.contains("goldfish");

        if(isGenericEmulator || isGenyMotion){
            return DIUtility.YES;
        }else {
            return DIUtility.NO;
        }
    }
}

