
package com.deviceinfo.extras;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build.VERSION;
import androidx.annotation.RequiresPermission;

/**
 * The type Easy fingerprint mod.
 */
public class Fingerprint {

    private FingerprintManager fingerprintManager;

    /**
     * Instantiates a new Easy fingerprint mod.
     *
     * You need to declare the below permission in the manifest file to use this properly
     *
     * <uses-permission android:name="android.permission.USE_FINGERPRINT" />
     *
     * @param context the context
     */
    @TargetApi(23)
    @RequiresPermission(permission.USE_FINGERPRINT)
    public Fingerprint(Context context) {
        if (VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            this.fingerprintManager =
                    (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        }
    }

    /**
     * Are fingerprints enrolled boolean.
     *
     * You need to declare the below permission in the manifest file to use this properly
     *
     * <uses-permission android:name="android.permission.USE_FINGERPRINT" />
     *
     * @return the boolean
     */
    @SuppressLint("NewApi")
    @RequiresPermission(permission.USE_FINGERPRINT)
    public final boolean areFingerprintsEnrolled() {
        return (fingerprintManager != null) && this.fingerprintManager.hasEnrolledFingerprints();
    }

    /**
     * Is fingerprint sensor present boolean.
     *
     * You need to declare the below permission in the manifest file to use this properly
     *
     * <uses-permission android:name="android.permission.USE_FINGERPRINT" />
     *
     * @return the boolean
     */
    @SuppressLint("NewApi")
    @RequiresPermission(permission.USE_FINGERPRINT)
    public final boolean isFingerprintSensorPresent() {
        return (fingerprintManager != null) && this.fingerprintManager.isHardwareDetected();
    }
}
