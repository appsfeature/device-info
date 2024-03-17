
package com.deviceinfo.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.StatFs;

import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

import static android.os.Build.VERSION_CODES;
import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getDataDirectory;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStorageState;

/**
 * EasyMemory Mod Class
 * <p>
 * Deprecation warning suppressed since it is handled in the code
 */
public class DeviceMemory {

    private static final String IO_EXCEPTION = "IO Exception";

    private static final int BYTEFACTOR = 1024;

    public DeviceMemory() {
    }

    public HashMap<String, String> getInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = new HashMap<>();
        info.put("total_internal_memory_size", convertToGbFormatted(getTotalInternalMemorySize()));
        info.put("available_internal_memory_size", convertToGbFormatted(getAvailableInternalMemorySize()));
        info.put("total_ram", convertToGbFormatted(getTotalRAM(context)));
        info.put("total_ram_memory_size", convertToGbFormatted(totalRamMemorySize(context)));
        info.put("free_ram_memory_size", convertToGbFormatted(freeRamMemorySize(context)));
        info.put("external_memory_available", externalMemoryAvailable());
        if(isExternalMemoryAvailable()) {
            info.put("total_external_memory_size", convertToGbFormatted(getTotalExternalMemorySize()));
            info.put("available_external_memory_size", convertToGbFormatted(getAvailableExternalMemorySize()));
        }
        DITimeLogger.timeLogging("Memory", startTime);
        return info;
    }

    /**
     * Gets available external memory size.
     *
     * @return the available external memory size
     */
    public final long getAvailableExternalMemorySize() {
        try {
            if (this.isExternalMemoryAvailable()) {
                final File path = getExternalStorageDirectory();
                final StatFs stat = new StatFs(path.getPath());
                final long blockSize;
                final long availableBlocks;
                if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                    blockSize = stat.getBlockSizeLong();
                    availableBlocks = stat.getAvailableBlocksLong();
                } else {
                    blockSize = stat.getBlockSize();
                    availableBlocks = stat.getAvailableBlocks();
                }
                return availableBlocks * blockSize;
            } else {
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * Gets available internal memory size.
     *
     * @return the available internal memory size
     */
    public final long getAvailableInternalMemorySize() {
        try {
            final File path = getDataDirectory();
            final StatFs stat = new StatFs(path.getPath());
            final long blockSize;
            final long availableBlocks;
            if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                blockSize = stat.getBlockSize();
                availableBlocks = stat.getAvailableBlocks();
            }
            return availableBlocks * blockSize;
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * Gets total external memory size.
     *
     * @return the total external memory size
     */
    public final long getTotalExternalMemorySize() {
        try {
            if (this.isExternalMemoryAvailable()) {
                final File path = getExternalStorageDirectory();
                final StatFs stat = new StatFs(path.getPath());
                final long blockSize;
                final long totalBlocks;
                if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                    blockSize = stat.getBlockSizeLong();
                    totalBlocks = stat.getBlockCountLong();
                } else {
                    blockSize = stat.getBlockSize();
                    totalBlocks = stat.getBlockCount();
                }
                return totalBlocks * blockSize;
            } else {
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * Gets total internal memory size.
     *
     * @return the total internal memory size
     */
    public final long getTotalInternalMemorySize() {
        try {
            final File path = getDataDirectory();
            final StatFs stat = new StatFs(path.getPath());
            final long blockSize;
            final long totalBlocks;
            if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
                totalBlocks = stat.getBlockCountLong();
            } else {
                blockSize = stat.getBlockSize();
                totalBlocks = stat.getBlockCount();
            }
            return totalBlocks * blockSize;
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * Gets total ram.
     *
     * @return the total ram
     */
    public final long getTotalRAM(Context context) {
        long totalMemory = 0;
        try {
            final ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            final ActivityManager activityManager =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                activityManager.getMemoryInfo(mi);
                totalMemory = mi.totalMem;
            }
        }catch (Exception e){
            return totalMemory;
        }
        return totalMemory;
    }

    public long totalRamMemorySize(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.totalMem;
    }

    public long freeRamMemorySize(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.availMem;
    }

    public String formatSize(long size) {
        if (size <= 0L) {
            return "0";
        } else {
            String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
            int digitGroups = (int)(Math.log10((double)size) / Math.log10(1024.0));
            return (new DecimalFormat("#,##0.#")).format((double)size / Math.pow(1024.0, (double)digitGroups)) + " " + units[digitGroups];
        }
    }

    public boolean isExternalMemoryAvailable() {
        try {
            return (getExternalStorageState().equals(MEDIA_MOUNTED));
        }catch (Exception e){
            return false;
        }
    }

    public String externalMemoryAvailable() {
        if (isExternalMemoryAvailable()) {
            return DIUtility.YES;
        } else {
            return DIUtility.NO;
        }
    }


    /**
     * Convert to gb float.
     *
     * @param valInBytes the val in bytes
     * @return the float
     */
    public String convertToGbFormatted(final long valInBytes) {
        return String.valueOf(convertToGb(valInBytes)+" Gb");
    }

    public float convertToGb(final long valInBytes) {
        return (float) valInBytes / (DeviceMemory.BYTEFACTOR * DeviceMemory.BYTEFACTOR * DeviceMemory.BYTEFACTOR);
    }

    /**
     * Convert to kb float.
     *
     * @param valInBytes the val in bytes
     * @return the float
     */
    public float convertToKb(final long valInBytes) {
        return (float) valInBytes / DeviceMemory.BYTEFACTOR;
    }

    /**
     * Convert to mb float.
     *
     * @param valInBytes the val in bytes
     * @return the float
     */
    public String convertToMbFormatted(final long valInBytes) {
        return String.valueOf(convertToMb(valInBytes)+" Mb");
    }
    public float convertToMb(final long valInBytes) {
        return (float) valInBytes / (DeviceMemory.BYTEFACTOR * DeviceMemory.BYTEFACTOR);
    }

    /**
     * Convert to tb float.
     *
     * @param valInBytes the val in bytes
     * @return the float
     */
    @SuppressWarnings("NumericOverflow")
    public float convertToTb(final long valInBytes) {
        return (float) valInBytes / (DeviceMemory.BYTEFACTOR * DeviceMemory.BYTEFACTOR * DeviceMemory.BYTEFACTOR
                * DeviceMemory.BYTEFACTOR);
    }

}
