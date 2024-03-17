package com.deviceinfo.util;

import android.text.TextUtils;

/**
 * Check Validity Util Class
 */
public final class DIValidityCheck {

    private DIValidityCheck() {
        // private constructor for utility class
    }

    /**
     * Check valid data string.
     *
     * @param data the data
     * @return the string
     */
    public static String checkValidData(String data) {
        if (TextUtils.isEmpty(data)) {
            return EasyDeviceInfo.NOT_FOUND_VAL;
        }
        return data;
    }

    /**
     * Check valid data string [ ].
     *
     * @param data the data
     * @return the string [ ]
     */
    public static String[] checkValidData(String[] data) {
        if ((data == null) || (data.length == 0)) {
           return new String[]{EasyDeviceInfo.NOT_FOUND_VAL};
        }
        return data;
    }

    /**
     * Handle illegal character in result string.
     *
     * @param result the result
     * @return the string
     */
    public static String handleIllegalCharacterInResult(String result) {
        if ((result != null) && result.contains(" ")) {
            return result.replaceAll(" ", "_");
        }
        return result;
    }
}
