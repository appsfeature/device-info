
package com.deviceinfo.device;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        PhoneType.GSM, PhoneType.CDMA, PhoneType.NONE
})
@Retention(RetentionPolicy.CLASS)
public @interface PhoneType {

    int GSM = 0;
    int CDMA = 1;
    int NONE = 2;
}
