package com.deviceinfo.device;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.deviceinfo.util.DIValidityCheck;
import com.deviceinfo.util.EasyDeviceInfo;
import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.PermissionUtility;
import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;
import static android.telephony.TelephonyManager.PHONE_TYPE_NONE;

/**
 * EasyDevice Mod Class
 */
public class UserDeviceInfo {

    private final TelephonyManager tm;

    public UserDeviceInfo(Context context) {
        this.tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public HashMap<String, String> getInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = new HashMap<>();
        info.put("android_os_version_name", getOSVersion());
        info.put("android_base_os_version_name", getBaseOSVersion());
        info.put("display_version", getDisplayVersion());
        info.put("build_board", getBoard());
        info.put("system_boot_loader_version", getBootloader());
        info.put("brand_name", getBuildBrand());
        info.put("host", getBuildHost());
        info.put("build_tags", getBuildTags());
        info.put("build_user", getBuildUser());
        info.put("version_code_name", getBuildVersionCodename());
        info.put("version_incremental", getBuildVersionIncremental());
        info.put("version_release", getBuildVersionRelease());
        info.put("build_device", getDevice());
        info.put("device_unique_fingerprint", getFingerprint());
        info.put("hardware", getHardware());
        info.put("language", getLanguage());
        info.put("manufacturer", getManufacturer());
        info.put("device_model", getModel());
        info.put("device_unique_id", getDeviceId(context));
        info.put("phone_no", getPhoneNo(context)); //required Permission
        info.put("product", getProduct());
        info.put("radio_version", getRadioVer());
        info.put("screen_display_id", getScreenDisplayID(context));
        info.put("serial", getSerial(context));//required Permission
        info.put("build_version_sdk", String.valueOf(getBuildVersionSDK()));
        info.put("device_type", getDeviceType(context));//required Activity
        info.put("phone_type", getPhoneType());
        info.put("phone_type_mod", getPhoneTypeModDetail());
        info.put("build_id", getBuildID());
        info.put("build_time", DIUtility.formattedDate(getBuildTime()));
        info.put("orientation", getOrientationDetail(context));
        info.put("is_device_rooted", isDeviceRooted() + "");
        DITimeLogger.timeLogging("Device", startTime);
        return info;
    }

    @SuppressLint("HardwareIds")
    public String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Gets board.
     *
     * @return the board
     */
    public final String getBoard() {
        return DIValidityCheck.checkValidData(Build.BOARD);
    }

    /**
     * Gets bootloader.
     *
     * @return the bootloader
     */
    public final String getBootloader() {
        return DIValidityCheck.checkValidData(Build.BOOTLOADER);
    }

    /**
     * Gets build brand.
     *
     * @return the build brand
     */
    public final String getBuildBrand() {
        return DIValidityCheck.checkValidData(
                DIValidityCheck.handleIllegalCharacterInResult(Build.BRAND));
    }

    /**
     * Gets build host.
     *
     * @return the build host
     */
    public final String getBuildHost() {
        return DIValidityCheck.checkValidData(Build.HOST);
    }

    /**
     * Gets build id.
     *
     * @return the build id
     */
    public final String getBuildID() {
        return DIValidityCheck.checkValidData(Build.ID);
    }

    /**
     * Gets build tags.
     *
     * @return the build tags
     */
    public final String getBuildTags() {
        return DIValidityCheck.checkValidData(Build.TAGS);
    }

    /**
     * Gets build time.
     *
     * @return the build time
     */
    public final long getBuildTime() {
        return Build.TIME;
    }

    /**
     * Gets build user.
     *
     * @return the build user
     */
    public final String getBuildUser() {
        return DIValidityCheck.checkValidData(Build.USER);
    }

    /**
     * Gets build version codename.
     *
     * @return the build version codename
     */
    public final String getBuildVersionCodename() {
        return DIValidityCheck.checkValidData(VERSION.CODENAME);
    }

    /**
     * Gets build version incremental.
     *
     * @return the build version incremental
     */
    public final String getBuildVersionIncremental() {
        return DIValidityCheck.checkValidData(VERSION.INCREMENTAL);
    }

    /**
     * Gets build version release.
     *
     * @return the build version release
     */
    public final String getBuildVersionRelease() {
        return DIValidityCheck.checkValidData(VERSION.RELEASE);
    }

    /**
     * Gets build version sdk.
     *
     * @return the build version sdk
     */
    public final int getBuildVersionSDK() {
        return VERSION.SDK_INT;
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    public final String getDevice() {
        return DIValidityCheck.checkValidData(Build.DEVICE);
    }

    public final String getDeviceType(final Context context) {
        if (context instanceof Activity) {
            switch (getDeviceType((Activity) context)) {
                case UserDeviceType.WATCH:
                    return "Watch";
                case UserDeviceType.PHABLET:
                    return "PHABLET";
                case UserDeviceType.PHONE:
                    return "PHONE";
                case UserDeviceType.TABLET:
                    return "TABLET";
                case UserDeviceType.TV:
                    return "TV";
                default:
                    return "Undefined";
            }
        }
        return "Need Activity Reference";
    }


    /**
     * Device type int.
     * Based on metric : <a href="https://design.google.com/devices/">Website</a>
     *
     * @param activity the activity
     * @return the int
     */
    @UserDeviceType
    public final int getDeviceType(final Activity activity) {
        final DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final float yInches = metrics.heightPixels / metrics.ydpi;
        final float xInches = metrics.widthPixels / metrics.xdpi;
        final double diagonalInches = Math.sqrt((xInches * xInches) + (yInches * yInches));
        if (diagonalInches > 10.1) {
            return UserDeviceType.TV;
        } else if ((diagonalInches <= 10.1) && (diagonalInches > 7)) {
            return UserDeviceType.TABLET;
        } else if ((diagonalInches <= 7) && (diagonalInches > 6.5)) {
            return UserDeviceType.PHABLET;
        } else if ((diagonalInches <= 6.5) && (diagonalInches >= 2)) {
            return UserDeviceType.PHONE;
        } else {
            return UserDeviceType.WATCH;
        }
    }

    /**
     * Gets display version.
     *
     * @return the display version
     */
    public final String getDisplayVersion() {
        return DIValidityCheck.checkValidData(Build.DISPLAY);
    }

    /**
     * Gets fingerprint.
     *
     * @return the fingerprint
     */
    public final String getFingerprint() {
        return DIValidityCheck.checkValidData(Build.FINGERPRINT);
    }

    /**
     * Gets hardware.
     *
     * @return the hardware
     */
    public final String getHardware() {
        return DIValidityCheck.checkValidData(Build.HARDWARE);
    }

    /**
     * Gets IMEI number
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return the imei
     * @deprecated
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(permission.READ_PHONE_STATE)
    @Deprecated
    public final String getIMEI(Context context) {
        try {

            String result = null;
            if (PermissionUtility.hasPermission(context, permission.READ_PHONE_STATE)) {
                result = this.tm.getDeviceId();
            }

            return DIValidityCheck.checkValidData(result);
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets language.
     *
     * @return the language
     */
    public final String getLanguage() {
        return DIValidityCheck.checkValidData(Locale.getDefault().getLanguage());
    }

    /**
     * Gets manufacturer.
     *
     * @return the manufacturer
     */
    public final String getManufacturer() {
        return DIValidityCheck.checkValidData(
                DIValidityCheck.handleIllegalCharacterInResult(Build.MANUFACTURER));
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public final String getModel() {
        return DIValidityCheck.checkValidData(
                DIValidityCheck.handleIllegalCharacterInResult(Build.MODEL));
    }

    /**
     * Gets os codename.
     *
     * @return the os codename
     */
    public final String getAndroidOSVersion() {
        final String codename;
        switch (VERSION.SDK_INT) {
            case VERSION_CODES.BASE:
                codename = "First Android Version. Yay !";
                break;
            case VERSION_CODES.BASE_1_1:
                codename = "Base Android 1.1";
                break;
            case VERSION_CODES.CUPCAKE:
                codename = "Cupcake";
                break;
            case VERSION_CODES.DONUT:
                codename = "Donut";
                break;
            case VERSION_CODES.ECLAIR:
            case VERSION_CODES.ECLAIR_0_1:
            case VERSION_CODES.ECLAIR_MR1:

                codename = "Eclair";
                break;
            case VERSION_CODES.FROYO:
                codename = "Froyo";
                break;
            case VERSION_CODES.GINGERBREAD:
            case VERSION_CODES.GINGERBREAD_MR1:
                codename = "Gingerbread";
                break;
            case VERSION_CODES.HONEYCOMB:
            case VERSION_CODES.HONEYCOMB_MR1:
            case VERSION_CODES.HONEYCOMB_MR2:
                codename = "Honeycomb";
                break;
            case VERSION_CODES.ICE_CREAM_SANDWICH:
            case VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
                codename = "Ice Cream Sandwich";
                break;
            case VERSION_CODES.JELLY_BEAN:
            case VERSION_CODES.JELLY_BEAN_MR1:
            case VERSION_CODES.JELLY_BEAN_MR2:
                codename = "Jelly Bean";
                break;
            case VERSION_CODES.KITKAT:
                codename = "Kitkat";
                break;
            case VERSION_CODES.KITKAT_WATCH:
                codename = "Kitkat Watch";
                break;
            case VERSION_CODES.LOLLIPOP:
            case VERSION_CODES.LOLLIPOP_MR1:
                codename = "Lollipop";
                break;
            case VERSION_CODES.M:
                codename = "Marshmallow";
                break;
            case VERSION_CODES.N:
            case VERSION_CODES.N_MR1:
                codename = "Nougat";
                break;
            case VERSION_CODES.O:
                codename = "Oreo";
                break;
            case VERSION_CODES.P:
                codename = "Pie";
                break;
            default:
                codename = EasyDeviceInfo.NOT_FOUND_VAL;
                break;
        }
        return codename;
    }

    /**
     * Gets os version.
     *
     * @return the os version
     */
    public final String getOSVersion() {
        return DIValidityCheck.checkValidData(VERSION.RELEASE);
    }

    public final String getBaseOSVersion() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            return DIValidityCheck.checkValidData(VERSION.BASE_OS);
        } else {
            return EasyDeviceInfo.NOT_FOUND_VAL;
        }
    }

    public final String getOrientationDetail(Context context) {
        switch (getOrientation(context)) {
            case OrientationType.LANDSCAPE:
                return "LANDSCAPE";
            case OrientationType.PORTRAIT:
                return "PORTRAIT";
            case OrientationType.UNKNOWN:
                return "UNKNOWN";
            default:
                return "Undefined";
        }
    }

    /**
     * Gets orientation.
     *
     * @return the orientation
     */
    @OrientationType
    public final int getOrientation(Context context) {
        switch (context.getResources().getConfiguration().orientation) {
            case ORIENTATION_PORTRAIT:
                return OrientationType.PORTRAIT;
            case ORIENTATION_LANDSCAPE:
                return OrientationType.LANDSCAPE;
            default:
                return OrientationType.UNKNOWN;
        }
    }

    /**
     * Gets phone no.
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return the phone no
     */
    @SuppressLint("HardwareIds")
    public final String getPhoneNo(Context context) {
        if (!PermissionUtility.hasPermission(context, permission.READ_PHONE_STATE)) {
            return "Permission Missing (READ_PHONE_STATE)";
        }
        try {
            String result = null;
            if (ActivityCompat.checkSelfPermission(context, permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "Permission Missing (READ_PHONE_STATE | READ_PHONE_NUMBERS | READ_SMS)";
            }
            result = this.tm.getLine1Number();

            return DIValidityCheck.checkValidData(result);
        } catch (NullPointerException e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets phone type.
     *
     * @return the phone type
     */
    public final String getPhoneType() {
        try {
            switch (this.tm.getPhoneType()) {
                case PHONE_TYPE_GSM:
                    return "GSM";

                case PHONE_TYPE_CDMA:
                    return "CDMA";
                case PHONE_TYPE_NONE:
                default:
                    return "Unknown";
            }
        } catch (NullPointerException e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    public final String getPhoneTypeModDetail() {
        switch (getPhoneTypeMod()) {
            case PhoneType.CDMA:
                return "CDMA";
            case PhoneType.GSM:
                return "GSM";
            default:
            case PhoneType.NONE:
                return "NONE";
        }
    }

    @PhoneType
    public final int getPhoneTypeMod() {
        try {
            switch (this.tm.getPhoneType()) {
                case PHONE_TYPE_GSM:
                    return PhoneType.GSM;

                case PHONE_TYPE_CDMA:
                    return PhoneType.CDMA;
                case PHONE_TYPE_NONE:
                default:
                    return PhoneType.NONE;
            }
        } catch (NullPointerException e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return PhoneType.NONE;
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return PhoneType.NONE;
        }
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    public final String getProduct() {
        try {
            return DIValidityCheck.checkValidData(Build.PRODUCT);
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets radio ver.
     *
     * @return the radio ver
     */
    public final String getRadioVer() {
        try {
            String result = null;
            result = Build.getRadioVersion();
            return DIValidityCheck.checkValidData(result);
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets screen display id.
     *
     * @return the screen display id
     */
    public final String getScreenDisplayID(Context context) {
        try {
            final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                final Display display = wm.getDefaultDisplay();
                return DIValidityCheck.checkValidData(String.valueOf(display.getDisplayId()));
            } else {
                return EasyDeviceInfo.NOT_FOUND_VAL;
            }
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets serial.
     *
     * @return the serial
     */
    @SuppressLint({"HardwareIds"})
    public final String getSerial(Context context) {
        try {
            String result = null;
            if (VERSION.SDK_INT < VERSION_CODES.O) {
                result = Build.SERIAL;
            } else {
                if (ActivityCompat.checkSelfPermission(context, permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "Permission Missing (READ_PHONE_STATE)";
                }
                result = Build.getSerial();
            }
            return DIValidityCheck.checkValidData(result);
        }catch (Exception e){
            DILogger.e(DIUtility.ERROR_TAG,e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Is Device rooted boolean.
     *
     * @return the boolean
     */
    public final boolean isDeviceRooted() {
        final String su = "su";
        final String[] locations = {
                "/sbin/", "/system/bin/", "/system/xbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"
        };
        for (final String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }
}
