package com.deviceinfo.config;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        RingerMode.SILENT, RingerMode.NORMAL, RingerMode.VIBRATE
})
@Retention(RetentionPolicy.CLASS)
public @interface RingerMode {

    String SILENT = "SILENT";
    String NORMAL = "NORMAL";
    String VIBRATE = "VIBRATE";
}
