package com.deviceinfo.permission.network;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.deviceinfo.util.DIValidityCheck;
import com.deviceinfo.util.EasyDeviceInfo;
import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.PermissionUtility;
import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Network {

    private static final String SOCKET_EXCEPTION = "Socket Exception";

    private final TelephonyManager tm;

    public Network(Context context) {
        this.tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public HashMap<String, String> getInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = new HashMap<>();

        try {
            // Permission Required
            info.put("wifi_enabled", isWifiEnabledDetail(context));
            info.put("mac_address", getMACAddress(context, "wlan0"));
            info.put("bssid", getWifiBSSID(context));
            info.put("connection_status", isNetworkAvailable(context));
            info.put("ip_v4_address", getIPv4Address(context));
            info.put("ip_v6_address", getIPv6Address(context));
            info.put("data_type", getDataType(context));
            info.put("ssid", getWifiSSID(context));
            info.put("link_speed", getWifiLinkSpeed(context));
            info.put("cell_tower", loadCellTowerInfo(context));
        } catch (Exception e) {
            DILogger.e(e.toString());
            info.put("exception", e.toString());
        }

        DITimeLogger.timeLogging("Network", startTime);
        return info;
    }

    /**
     * Gets ip address v4.
     *
     * @return the ip address
     */
    public final String getIPv4Address(Context context) {
        if (!PermissionUtility.hasPermission(context, permission.INTERNET) && !PermissionUtility.hasPermission(context, permission.ACCESS_NETWORK_STATE)) {
            return "Permission Missing (INTERNET, ACCESS_NETWORK_STATE)";
        }
        try {
            String result = null;
            final List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (final NetworkInterface intf : interfaces) {
                final List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (final InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        final String sAddr = addr.getHostAddress().toUpperCase(Locale.getDefault());
                        final boolean isIPv4 = addr instanceof Inet4Address;
                        if (isIPv4) {
                            result = sAddr;
                        }
                    }
                }
            }
            return DIValidityCheck.checkValidData(result);
        } catch (final SocketException e) {
            if (EasyDeviceInfo.debuggable) {
                DILogger.e(EasyDeviceInfo.nameOfLib, Network.SOCKET_EXCEPTION, e);
            }
            return e.getMessage();
        }
    }

    /**
     * Gets ip address v6.
     *
     * @return the ip address
     */
    public final String getIPv6Address(Context context) {
        if (!PermissionUtility.hasPermission(context, permission.INTERNET) && !PermissionUtility.hasPermission(context, permission.ACCESS_NETWORK_STATE)) {
            return "Permission Missing (INTERNET, ACCESS_NETWORK_STATE)";
        }
        try {
            String result = null;
            final List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (final NetworkInterface intf : interfaces) {
                final List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (final InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        final String sAddr = addr.getHostAddress().toUpperCase(Locale.getDefault());
                        final boolean isIPv4 = addr instanceof Inet4Address;
                        if (!isIPv4) {
                            final int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                            result = (delim < 0) ? sAddr : sAddr.substring(0, delim);
                        }
                    }
                }
            }
            return DIValidityCheck.checkValidData(result);
        } catch (final SocketException e) {
            if (EasyDeviceInfo.debuggable) {
                DILogger.e(EasyDeviceInfo.nameOfLib, Network.SOCKET_EXCEPTION, e);
            }
            return e.getMessage();
        }
    }

    public final String getDataType(Context context) {
        String networkType = "unknown";
        try {
            switch (getNetworkType(context)) {
                case NetworkType.CELLULAR_2G:
                    networkType = "2G";
                    break;
                case NetworkType.CELLULAR_3G:
                    networkType = "3G";
                    break;
                case NetworkType.CELLULAR_4G:
                    networkType = "4G";
                    break;
                case NetworkType.WIFI_WIFIMAX:
                    networkType = "WiFi";
                    break;
                case NetworkType.CELLULAR_UNIDENTIFIED_GEN:
                    break;
                case NetworkType.CELLULAR_UNKNOWN:
                    break;
                case NetworkType.UNKNOWN:
                    break;
            }
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            networkType = e.getMessage();
        }
        return networkType;
    }

    /**
     * Gets network type.
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     * <uses-permission android:name="android.permission.INTERNET"/>
     *
     * @return the network type
     */
//    @RequiresPermission(allOf = {
//            permission.ACCESS_NETWORK_STATE, permission.INTERNET
//    })
    @NetworkType
    @SuppressLint("MissingPermission")
    public final int getNetworkType(Context context) throws Exception {
        int result = NetworkType.UNKNOWN;
        if (PermissionUtility.hasPermission(context, permission.INTERNET) && PermissionUtility.hasPermission(context, permission.ACCESS_NETWORK_STATE)) {
            final ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork == null) {
                    result = NetworkType.UNKNOWN;
                } else if ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                        || (activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX)) {
                    result = NetworkType.WIFI_WIFIMAX;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    final TelephonyManager manager =
                            (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                    if ((manager != null) && (manager.getSimState() == TelephonyManager.SIM_STATE_READY)) {
                        switch (manager.getNetworkType()) {

                            // Unknown
                            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                                result = NetworkType.CELLULAR_UNKNOWN;
                                break;
                            // Cellular Data 2G
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                                result = NetworkType.CELLULAR_2G;
                                break;
                            // Cellular Data 3G
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                result = NetworkType.CELLULAR_3G;
                                break;
                            // Cellular Data 4G
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                result = NetworkType.CELLULAR_4G;
                                break;
                            // Cellular Data Unknown Generation
                            default:
                                result = NetworkType.CELLULAR_UNIDENTIFIED_GEN;
                                break;
                        }
                    }
                }
            }
        } else {
            throw new Exception("Permission Missing (INTERNET, ACCESS_NETWORK_STATE)");
        }
        return result;
    }

    /**
     * Gets BSSID of Connected WiFi
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     *
     * @return Return the basic service set identifier (BSSID) of the current access point.
     */
//    @RequiresPermission(allOf = {
//            permission.ACCESS_WIFI_STATE, permission.ACCESS_NETWORK_STATE
//    })
    @SuppressLint("MissingPermission")
    public final String getWifiBSSID(Context context) {
        String result = null;
        if (PermissionUtility.hasPermission(context, permission.ACCESS_NETWORK_STATE) && PermissionUtility.hasPermission(context, permission.ACCESS_WIFI_STATE)) {
            final ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo == null) {
                    result = null;
                }

                if ((networkInfo != null) && networkInfo.isConnected()) {
                    WifiManager wifiManager =
                            (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (wifiManager != null) {
                        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                        if ((connectionInfo != null) && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                            result = connectionInfo.getBSSID();
                        }
                    }
                }
            }
        } else {
            result = "Permission Missing (ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE)";
        }
        return DIValidityCheck.checkValidData(result);
    }

    /**
     * Gets Link Speed of Connected WiFi
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     *
     * @return link speed
     */
//    @RequiresPermission(allOf = {
//            permission.ACCESS_WIFI_STATE, permission.ACCESS_NETWORK_STATE
//    })
    @SuppressLint("MissingPermission")
    public final String getWifiLinkSpeed(Context context) {
        String result = null;
        if (PermissionUtility.hasPermission(context, permission.ACCESS_NETWORK_STATE) && PermissionUtility.hasPermission(context, permission.ACCESS_WIFI_STATE)) {
            final ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo == null) {
                    result = null;
                }

                if ((networkInfo != null) && networkInfo.isConnected()) {
                    WifiManager wifiManager =
                            (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (wifiManager != null) {
                        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                        if ((connectionInfo != null) && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                            result = connectionInfo.getLinkSpeed() + " Mbps";
                        }
                    }
                }
            }
        } else {
            result = "Permission Missing (ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE)";
        }
        return DIValidityCheck.checkValidData(result);
    }

    /**
     * Gets WiFi MAC Address
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     *
     * @return the wifi mac
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(permission.ACCESS_WIFI_STATE)
    public final String getWifiMAC(Context context) {
        String result = "02:00:00:00:00:00";
        if (PermissionUtility.hasPermission(context, permission.ACCESS_WIFI_STATE)) {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Hardware ID are restricted in Android 6+
                // https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id
                Enumeration<NetworkInterface> interfaces = null;
                try {
                    interfaces = NetworkInterface.getNetworkInterfaces();
                } catch (final SocketException e) {
                    if (EasyDeviceInfo.debuggable) {
                        DILogger.e(EasyDeviceInfo.nameOfLib, Network.SOCKET_EXCEPTION, e);
                    }
                }
                while ((interfaces != null) && interfaces.hasMoreElements()) {
                    final NetworkInterface networkInterface = interfaces.nextElement();

                    byte[] addr = new byte[0];
                    try {
                        addr = networkInterface.getHardwareAddress();
                    } catch (final SocketException e) {
                        if (EasyDeviceInfo.debuggable) {
                            DILogger.e(EasyDeviceInfo.nameOfLib, Network.SOCKET_EXCEPTION, e);
                        }
                    }
                    if ((addr == null) || (addr.length == 0)) {
                        continue;
                    }

                    final StringBuilder buf = new StringBuilder();
                    for (final byte b : addr) {
                        buf.append(String.format("%02X:", Byte.valueOf(b)));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    final String mac = buf.toString();
                    final String wifiInterfaceName = "wlan0";
                    result = wifiInterfaceName.equals(networkInterface.getName()) ? mac : result;
                }
            } else {
                final WifiManager wm =
                        (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wm != null) {
                    result = wm.getConnectionInfo().getMacAddress();
                }
            }
        }
        return DIValidityCheck.checkValidData(result);
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public String getMACAddress(Context context, String interfaceName) {
        if (!PermissionUtility.hasPermission(context, permission.ACCESS_WIFI_STATE)
                || !PermissionUtility.hasPermission(context, permission.INTERNET)
                || !PermissionUtility.hasPermission(context, permission.ACCESS_NETWORK_STATE)) {
            return "Permission Missing (INTERNET, ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE)";
        }
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:", aMac));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
            DILogger.e(ex.toString());
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Gets SSID of Connected WiFi
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     *
     * @return Returns the service set identifier (SSID) of the current 802.11 network
     */
//    @RequiresPermission(allOf = {
//            permission.ACCESS_WIFI_STATE, permission.ACCESS_NETWORK_STATE
//    })
    @SuppressLint("MissingPermission")
    public final String getWifiSSID(Context context) {
        String result = null;
        if (PermissionUtility.hasPermission(context, permission.ACCESS_WIFI_STATE) && PermissionUtility.hasPermission(context, permission.ACCESS_NETWORK_STATE)) {
            final ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo == null) {
                    result = null;
                }
                if ((networkInfo != null) && networkInfo.isConnected()) {
                    WifiManager wifiManager =
                            (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (wifiManager != null) {
                        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                        if ((connectionInfo != null) && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                            result = connectionInfo.getSSID();
                        }
                    }
                }
            }
        } else {
            result = "Permission Missing (ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE)";
        }
        return DIValidityCheck.checkValidData(result);
    }

    /**
     * Is network available boolean.
     * <p>
     * You need to declare the below permission in the manifest file to use this properly
     * <p>
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     * <uses-permission android:name="android.permission.INTERNET"/>
     *
     * @return the boolean
     */
//    @RequiresPermission(allOf = {
//            permission.ACCESS_NETWORK_STATE, permission.INTERNET
//    })
    @SuppressLint("MissingPermission")
    public final String isNetworkAvailable(Context context) {
        String result = null;
        if (PermissionUtility.hasPermission(context, permission.INTERNET)
                && PermissionUtility.hasPermission(context, permission.ACCESS_NETWORK_STATE)) {
            final ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                final NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnected()) {
                    result = "Connected";
                } else {
                    result = "Not connected";
                }
            }
        } else {
            result = "Permission Missing (INTERNET, ACCESS_NETWORK_STATE)";
        }
        return DIValidityCheck.checkValidData(result);
    }

    @RequiresPermission(allOf = {
            permission.ACCESS_NETWORK_STATE, permission.INTERNET
    })
    public final boolean isNetworkAvailable2(Context context) {
        if (PermissionUtility.hasPermission(context, permission.INTERNET)
                && PermissionUtility.hasPermission(context, permission.ACCESS_NETWORK_STATE)) {
            final ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                final NetworkInfo netInfo = cm.getActiveNetworkInfo();
                return (netInfo != null) && netInfo.isConnected();
            }
        } else {

        }
        return false;
    }

    /**
     * @return true if a Wi-Fi Aware compatible chipset is present in the device.
     * @see <a href="https://developer.android.com/guide/topics/connectivity/wifi-aware.html">https://developer.android.com/guide/topics/connectivity/wifi-aware.html</a>
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public final boolean isWifiAwareAvailable(Context context) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) && context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE);
    }

    /**
     * Is wifi enabled.
     *
     * @return the boolean
     */
    public final String isWifiEnabledDetail(Context context) {
        if (!PermissionUtility.hasPermission(context, permission.ACCESS_WIFI_STATE)) {
            return "Permission Missing (ACCESS_WIFI_STATE)";
        }
        if (isWifiEnabled(context)) {
            return "YES";
        } else {
            return "NO";
        }
    }

    public final boolean isWifiEnabled(Context context) {
        boolean wifiState = false;
        final WifiManager wifiManager =
                (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiState = wifiManager.isWifiEnabled();
        }
        return wifiState;
    }


    @SuppressLint("MissingPermission")
    public String loadCellTowerInfo(Context context) {
        JSONObject networkInfo = new JSONObject();
        if (!PermissionUtility.hasPermission(context, permission.ACCESS_FINE_LOCATION)) {
            return "Permission Missing (ACCESS_FINE_LOCATION)";
        }
        int lCurrentApiVersion = android.os.Build.VERSION.SDK_INT;
        try {
            List<CellInfo> cellInfoList = tm.getAllCellInfo();
            if (cellInfoList != null) {
                for (final CellInfo info : cellInfoList) {
                    int cId = 0, lac = 0;

                    if (info instanceof CellInfoGsm) {
                        final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                        final CellIdentityGsm identityGsm = ((CellInfoGsm) info).getCellIdentity();
                        // Signal Strength
                        networkInfo.put("signal_strength", String.valueOf(gsm.getDbm()));
                        // Cell Identity
                        networkInfo.put("gsm_cell_identity", String.valueOf(identityGsm.getCid()));
                        networkInfo.put("mobile_country_code", String.valueOf(identityGsm.getMcc()));
                        networkInfo.put("mobile_network_code", String.valueOf(identityGsm.getMnc()));
                        networkInfo.put("location_area_code", String.valueOf(identityGsm.getLac()));
                        cId = identityGsm.getCid();
                        lac = identityGsm.getLac();
                    } else if (info instanceof CellInfoCdma) {
                        final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                        final CellIdentityCdma identityCdma = ((CellInfoCdma) info).getCellIdentity();

                        // Signal Strength
                        networkInfo.put("signal_strength", String.valueOf(cdma.getDbm()));
                        // Cell Identity
                        networkInfo.put("gsm_cell_identity", String.valueOf(identityCdma.getBasestationId()));
                        networkInfo.put("mobile_network_code", String.valueOf(identityCdma.getSystemId()));
                        networkInfo.put("location_area_code", String.valueOf(identityCdma.getNetworkId()));
                        cId = identityCdma.getBasestationId();
                        lac = identityCdma.getNetworkId();
                    } else if (info instanceof CellInfoLte) {
                        final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                        final CellIdentityLte identityLte = ((CellInfoLte) info).getCellIdentity();

                        // Signal Strength
                        networkInfo.put("signal_strength", String.valueOf(lte.getDbm()));
                        // Cell Identity
                        networkInfo.put("gsm_cell_identity", String.valueOf(identityLte.getCi()));
                        networkInfo.put("mobile_country_code", String.valueOf(identityLte.getMcc()));
                        networkInfo.put("mobile_network_code", String.valueOf(identityLte.getMnc()));
                        networkInfo.put("location_area_code", "unknown");
                        cId = identityLte.getCi();
                        lac = 0;
                    } else if (lCurrentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2 && info instanceof CellInfoWcdma) {
                        final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                        final CellIdentityWcdma identityWcdma = ((CellInfoWcdma) info).getCellIdentity();

                        // Signal Strength
                        networkInfo.put("signal_strength", String.valueOf(wcdma.getDbm()));
                        // Cell Identity
                        networkInfo.put("gsm_cell_identity", String.valueOf(identityWcdma.getCid()));
                        networkInfo.put("mobile_country_code", String.valueOf(identityWcdma.getMcc()));
                        networkInfo.put("mobile_network_code", String.valueOf(identityWcdma.getMnc()));
                        networkInfo.put("location_area_code", String.valueOf(identityWcdma.getLac()));
                        cId = identityWcdma.getCid();
                        lac = identityWcdma.getLac();
                    } else {
                        networkInfo.put("cell_detail", "Unknown type of cell signal!");
                    }

                    if (isValid(cId, lac)) {
                        break;
                    }
                }

            }
        } catch (NullPointerException | JSONException npe) {
            return "Unknown type of cell signal!";
        }
        return networkInfo.toString();
    }

    public boolean isValid(int cId, int lac) {
        return cId != Integer.MAX_VALUE && lac != Integer.MAX_VALUE;
    }
}
