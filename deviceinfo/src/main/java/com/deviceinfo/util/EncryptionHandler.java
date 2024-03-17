package com.deviceinfo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.provider.Settings;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Abhijit on 9/18/2018.
 */

public class EncryptionHandler {

    public static String getAppVersion(Context context) {
        String appVersion = "0";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersion = pInfo.getLongVersionCode() + "";
            } else {
                appVersion = pInfo.versionCode + "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            DILogger.e(e.toString());
        }
        return appVersion;
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


    public static SecretKey generateKey(Context context)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(getSecurityCodeByte(context), "ARC4");
    }

    public static byte[] encryptMsg(Context context, String message)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        /* Encrypt the message. */
        Cipher cipher = null;
//        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher = Cipher.getInstance("ARC4");
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(context));
        return cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
    }

    public static String decryptMsg(Context context, byte[] cipherText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("ARC4");
        cipher.init(Cipher.DECRYPT_MODE, generateKey(context));
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }

    public static byte[] getSecurityCodeByte(Context ctx) {
        try {
            Signature[] signatures;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                signatures = info.signingInfo.getSigningCertificateHistory();
            } else {
                PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
                signatures = info.signatures;
            }
            for (Signature signature : signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(signature.toByteArray());
                byte[] bytes = md.digest();
//                Logger.e("printHashKey ff", Base64.encodeToString(bytes, Base64.URL_SAFE));
//                Logger.e("printHashKey HEXA", bytesToHex(bytes));
                return bytes;
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }


    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
