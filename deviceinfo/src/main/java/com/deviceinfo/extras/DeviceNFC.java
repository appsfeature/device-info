
package com.deviceinfo.extras;

import android.content.Context;
import android.nfc.NfcAdapter;

/**
 * EasyNfc Mod Class
 */
public class DeviceNFC {

    private final NfcAdapter nfcAdapter;

    public DeviceNFC(Context context) {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    /**
     * Is nfc enabled boolean.
     *
     * @return the boolean
     */
    public final String nfcEnabled() {
        if(isNfcEnabled()){
            return "Available";
        }else {
            return "Not available";
        }
    }

    public final boolean isNfcEnabled() {
        return (nfcAdapter != null) && this.nfcAdapter.isEnabled();
    }

    /**
     * Is nfc present boolean.
     *
     * @return the boolean
     */
    public final boolean isNfcPresent() {
        return nfcAdapter != null;
    }
}
