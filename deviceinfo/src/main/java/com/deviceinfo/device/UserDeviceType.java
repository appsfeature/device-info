
package com.deviceinfo.device;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        UserDeviceType.WATCH, UserDeviceType.PHONE, UserDeviceType.PHABLET, UserDeviceType.TABLET, UserDeviceType.TV
})
@Retention(RetentionPolicy.CLASS)
public @interface UserDeviceType {

    int WATCH = 0;
    int PHONE = 1;
    int PHABLET = 2;
    int TABLET = 3;
    int TV = 4;
}
