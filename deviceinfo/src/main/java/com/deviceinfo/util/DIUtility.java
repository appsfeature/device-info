package com.deviceinfo.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Date;

public class DIUtility {
    public static final String ERROR_TAG = "DeviceInfoLog";
    public static final String UNKNOWN = EasyDeviceInfo.NOT_FOUND_VAL;
    public static final String YES = "YES";
    public static final String NO = "NO";

    public static String formattedDate(long millisecond) {
        try {
            return DateFormat.format("dd-MM-yyyy | hh:mm a", new Date(millisecond)).toString();
        }catch (Exception e){
            return "Invalid time format";
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
