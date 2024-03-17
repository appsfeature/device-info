package com.deviceinfo.application;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;

import com.deviceinfo.util.DIValidityCheck;
import com.deviceinfo.util.EasyDeviceInfo;
import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Application {
    private static final String NAME_NOT_FOUND_EXCEPTION = "Name Not Found Exception";

    public Application() {
    }

    public List<Apps> getInfo(Context context) {
        return getApplicationsInfo(context);
    }


    public List<Apps> getApplicationsInfo(Context context) {
        long startTime = DITimeLogger.getStartTime();
        List<Apps> apps = new ArrayList<>();
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
            Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));
            for (ResolveInfo ri : activities) {
                Apps appInfo = Apps.Builder()
                        .setPackageName(getAppPackageName(ri))
                        .setName(String.valueOf(ri.loadLabel(pm)))
                        .setLabel(ri.activityInfo.name)
                        .setIcon(ri.activityInfo.loadIcon(pm));

                try {
                    PackageInfo info = pm.getPackageInfo(appInfo.getPackageName(), 0);
                    appInfo.setVersion(info.versionName);
                    appInfo.setVersionCode(String.valueOf(info.versionCode));
                    appInfo.setLastModified(info.lastUpdateTime);
                    appInfo.setInstalledDate(info.firstInstallTime);
                } catch (NameNotFoundException e) {
                    DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
                    appInfo.setVersion(e.getMessage());
                }

                appInfo.setReqPermission(getGrantedPermissions(pm, appInfo.getPackageName()));
                Intent launchedActivity = pm.getLaunchIntentForPackage(appInfo.getPackageName());
                if (launchedActivity != null && launchedActivity.getComponent() != null) {
                    String className = launchedActivity.getComponent().getClassName();
                    appInfo.setLaunchActivity(className);
                } else {
                    appInfo.setLaunchActivity("");
                }
                appInfo.setActivities(getActivities(pm, appInfo.getPackageName()));

                appInfo.setProviders(getProviders(pm, appInfo.getPackageName()));
                appInfo.setServices(getServices(pm, appInfo.getPackageName()));
                apps.add(appInfo);
            }
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            apps.add(Apps.Builder()
                    .setName(e.getMessage()));
        }
        DITimeLogger.timeLogging("Application", startTime);
        return apps;
    }

    private String getAppPackageName(ResolveInfo ri) {
        try {
            return ri.activityInfo.packageName;
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return "";
        }
    }

    public String getGrantedPermissions(PackageManager pm, final String appPackage) {
        try {
            List<String> granted = new ArrayList<>();
            PackageInfo pi = pm.getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i].substring(pi.requestedPermissions[i].lastIndexOf(".") + 1));
                }
            }
            return TextUtils.join(", ", granted);
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return "";
        }
    }

    public List<String> getAllPermissions(Context context, final String appPackage) {
        PackageManager pm = context.getPackageManager();
        List<String> granted = new ArrayList<>();
        try {
            PackageInfo pi = pm.getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            granted.addAll(Arrays.asList(pi.requestedPermissions));
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
        }
        return granted;
    }


    public String getActivities(PackageManager pm, final String appPackage) {
        try {
            List<String> granted = new ArrayList<>();
            PackageInfo pi = pm.getPackageInfo(appPackage, PackageManager.GET_ACTIVITIES);
            for (int i = 0; i < pi.activities.length; i++) {
                ActivityInfo data = pi.activities[i];
                granted.add(data.name);
            }
            return TextUtils.join(",", granted);
        } catch (NameNotFoundException e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return "";
        }
    }

    public String getProviders(PackageManager pm, final String appPackage) {
        try {
            List<String> list = new ArrayList<>();
            PackageInfo pi = pm.getPackageInfo(appPackage, PackageManager.GET_PROVIDERS);
            for (int i = 0; i < pi.providers.length; i++) {
                ProviderInfo data = pi.providers[i];
                list.add(data.name);
            }
            return TextUtils.join(",", list);
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return "";
        }
    }

    public String getServices(PackageManager pm, final String appPackage) {
        try {
            List<String> list = new ArrayList<>();
            PackageInfo pi = pm.getPackageInfo(appPackage, PackageManager.GET_SERVICES);
            for (int i = 0; i < pi.services.length; i++) {
                ServiceInfo data = pi.services[i];
                list.add(data.name);
            }
            return TextUtils.join(",", list);
        } catch (Exception e) {
            return "";
        }
    }

//
//    public static List<ApplicationInfo> getApplicationsInfo(Context context) {
//        List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();
//
//        Intent intent = new Intent(Intent.ACTION_MAIN, null);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        PackageManager pm = context.getPackageManager();
//        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
//        Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));
//        for (ResolveInfo ri : activities) {
//            ApplicationInfo info = new ApplicationInfo();
//            info.packageName = ri.activityInfo.packageName;
//            info.name = ri.activityInfo.name;
//            info.lab = (String) ri.loadLabel(pm);
//            info.icon = ri.activityInfo.loadIcon(pm);
//            apps.add(info);
//        }
//        return apps;
//    }

    /**
     * Gets activity name.
     */
    public final String getActivityName(Context context) {
        try {
            return DIValidityCheck.checkValidData(context.getClass().getSimpleName());
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets app name.
     */
    public final String getAppName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            String result = (ai != null) ? (String) pm.getApplicationLabel(ai) : null;
            return DIValidityCheck.checkValidData(result);
        } catch (final NameNotFoundException e) {
            if (EasyDeviceInfo.debuggable) {
                DILogger.d(EasyDeviceInfo.nameOfLib, Application.NAME_NOT_FOUND_EXCEPTION, e);
            }
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets app version.
     */
    public final String getAppVersion(Context context) {
        try {
            String result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return DIValidityCheck.checkValidData(result);
        } catch (final NameNotFoundException e) {
            if (EasyDeviceInfo.debuggable) {
                DILogger.e(EasyDeviceInfo.nameOfLib, Application.NAME_NOT_FOUND_EXCEPTION, e);
            }
            return e.getMessage();
        }
    }

    /**
     * Gets app version code.
     */
    public final String getAppVersionCode(Context context) {
        try {
            String result = String.valueOf(
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            return DIValidityCheck.checkValidData(result);
        } catch (final NameNotFoundException e) {
            if (EasyDeviceInfo.debuggable) {
                DILogger.e(EasyDeviceInfo.nameOfLib, Application.NAME_NOT_FOUND_EXCEPTION, e);
            }
            return e.getMessage();
        }
    }


    /**
     * Gets store.
     */
    public final String getStore(Context context) {
        try {
            final String result = context.getPackageManager().getInstallerPackageName(context.getPackageName());
            return DIValidityCheck.checkValidData(result);
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Check if the app with the specified package name is installed or not
     */
    public final boolean isAppInstalled(Context context, final String packageName) {
        try {
            return context.getPackageManager().getLaunchIntentForPackage(packageName) != null;
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return false;
        }
    }

    /**
     * Is permission granted boolean.
     */
    public final boolean isPermissionGranted(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
