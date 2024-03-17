package com.deviceinfo.util;

/**
 * Easy device info class.
 */
public final class EasyDeviceInfo {

    /**
     * The Name.
     */
    public static final String nameOfLib = "DeviceInfo";

    /**
     * The constant debuggable.
     */
    public static boolean debuggable;

    /**
     * The Not found val.
     */
    public static String NOT_FOUND_VAL = "unknown";

    /**
     * Debug.
     */
    public static void debug() {
        debuggable = true;
    }

    /**
     * Gets library version.
     *
     * @return the library version
     */
//    public static String getLibraryVersion() {
//        return EasyDeviceInfo.nameOfLib + " : v" + BuildConfig.VERSION_NAME + " [build-v" + BuildConfig.VERSION_CODE + ']';
//    }

    /**
     * Instantiates a new Easy device info.
     *
     * @param notFoundVal the not found val
     * @param debugFlag   the debug flag
     */
    public static void setConfigs(final String notFoundVal, final boolean debugFlag) {
        EasyDeviceInfo.NOT_FOUND_VAL = notFoundVal;
        debuggable = debugFlag;
    }

    /**
     * Instantiates a new Easy device info.
     *
     * @param notFoundVal the not found val
     */
    public static void setNotFoundVal(final String notFoundVal) {
        EasyDeviceInfo.NOT_FOUND_VAL = notFoundVal;
    }

    private EasyDeviceInfo() {

    }
}
