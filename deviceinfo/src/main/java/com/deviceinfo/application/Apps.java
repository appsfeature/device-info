package com.deviceinfo.application;

import android.graphics.drawable.Drawable;

import com.deviceinfo.util.DIUtility;

public class Apps {

    private String name;
    private Drawable icon;
    private String packageName;
    private String version;
    private String versionCode;
    private String targetSdkVersion;
    private long installedDate;
    private long lastModified;
    private String app_installed_date_formatted;
    private String app_last_modified_date_formatted;
    private String label;
    private String launchActivity;


    private String reqPermission;
    private String providers;
    private String services;
    private String activities;


    public String getProviders() {
        return providers;
    }

    public void setProviders(String providers) {
        this.providers = providers;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public Apps setVersionCode(String versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    public String getLaunchActivity() {
        return launchActivity;
    }

    public Apps setLaunchActivity(String launchActivity) {
        this.launchActivity = launchActivity;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Apps setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getName() {
        return name;
    }

    public Apps setName(String name) {
        this.name = name;
        return this;
    }

    public Drawable getIcon() {
        return icon;
    }

    public Apps setIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public Apps setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Apps setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getReqPermission() {
        return reqPermission;
    }

    public Apps setReqPermission(String reqPermission) {
        this.reqPermission = reqPermission;
        return this;
    }

    public String getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public Apps setTargetSdkVersion(String targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
        return this;
    }

    public long getInstalledDate() {
        return installedDate;
    }

    public Apps setInstalledDate(long installedDate) {
        this.installedDate = installedDate;
        setApp_installed_date_formatted(DIUtility.formattedDate(installedDate));
        return this;
    }
    public String getApp_installed_date_formatted() {
        return app_installed_date_formatted;
    }

    public void setApp_installed_date_formatted(String app_installed_date_formatted) {
        this.app_installed_date_formatted = app_installed_date_formatted;
    }

    public long getLastModified() {
        return lastModified;
    }

    public Apps setLastModified(long lastModified) {
        this.lastModified = lastModified;
        setApp_last_modified_date_formatted(DIUtility.formattedDate(lastModified));
        return this;
    }

    public String getApp_last_modified_date_formatted() {
        return app_last_modified_date_formatted;
    }

    public void setApp_last_modified_date_formatted(String app_last_modified_date_formatted) {
        this.app_last_modified_date_formatted = app_last_modified_date_formatted;
    }

    public static Apps Builder(){
        return new Apps();
    }

}
