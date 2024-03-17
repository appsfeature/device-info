package com.deviceinfo.display;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.deviceinfo.util.DIValidityCheck;
import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;

import java.util.HashMap;


public class DeviceDisplay {

    private final Display display;

    public DeviceDisplay(Context context) {
        this.display = getDisplayObject(context);
    }


    public HashMap<String, String> getInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = new HashMap<>();
        info.put("density", getDensity(context));
        info.put("physical_size", getPhysicalSize());
        info.put("orientation", getDeviceOrientation(context));
        info.put("layout_direction", String.valueOf(getLayoutDirection(context)));
        info.put("resolution", getResolution());
        info.put("screen_round", String.valueOf(isScreenRound(context)));
        info.put("refresh_rate", getRefreshRate());
        DITimeLogger.timeLogging("Display", startTime);
        return info;
    }

    public Display getDisplayObject(Context context) {
        try {
            final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            return wm != null ? wm.getDefaultDisplay() : null;
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return null;
        }
    }

    /**
     * Gets density.
     */
    public final String getDensity(Context context) {
        try {
            String densityStr;
            int density = context.getResources().getDisplayMetrics().densityDpi;
            switch (density) {
                case DisplayMetrics.DENSITY_LOW:
                    densityStr = "LDPI";
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    densityStr = "MDPI";
                    break;
                case DisplayMetrics.DENSITY_TV:
                    densityStr = "TVDPI";
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    densityStr = "HDPI";
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    densityStr = "XHDPI";
                    break;
                case DisplayMetrics.DENSITY_400:
                    densityStr = "XMHDPI";
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    densityStr = "XXHDPI";
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    densityStr = "XXXHDPI";
                    break;
                default:
                    densityStr = "";
                    break;
            }
            return DIValidityCheck.checkValidData(densityStr);
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Get display xy coordinates int [ ].
     */
    public final int[] getDisplayXYCoordinates(MotionEvent event) {
        final int[] coordinates = new int[2];
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            coordinates[0] = (int) event.getX();     // X Coordinates
            coordinates[1] = (int) event.getY();     // Y Coordinates
        }
        return coordinates;
    }

    public final int getLayoutDirection(Context context) {
        return context.getResources().getConfiguration().getLayoutDirection();
    }

    public final String getDeviceOrientation(Context context) {
        try {
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                return "portrait";
            } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return "landscape";
            } else {
                return "undefined";
            }
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    public final int getOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public final String getPhysicalSize() {
        float result = 0.0f;
        try {
            final DisplayMetrics metrics = new DisplayMetrics();
            if (display != null) {
                this.display.getMetrics(metrics);
                final float x = (float) StrictMath.pow(metrics.widthPixels / metrics.xdpi, 2);
                final float y = (float) StrictMath.pow(metrics.heightPixels / metrics.ydpi, 2);
                result = (float) Math.sqrt(x + y);
            }
        } catch (NullPointerException e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
        }
        return String.valueOf(result);
    }

    public final String getRefreshRate() {
        try {
            return String.valueOf(display.getRefreshRate());
        } catch (NullPointerException e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets resolution.
     */
    public final String getResolution() {
        try {
            final DisplayMetrics metrics = new DisplayMetrics();
            if (display != null) {
                this.display.getMetrics(metrics);
                return DIValidityCheck.checkValidData(metrics.heightPixels + "x" + metrics.widthPixels);
            } else {
                return DIValidityCheck.checkValidData("");
            }
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }

    }

    public final boolean isScreenRound(Context context) {
        try {
            return VERSION.SDK_INT >= Build.VERSION_CODES.M && context.getResources().getConfiguration()
                    .isScreenRound();
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return false;
        }
    }

}
