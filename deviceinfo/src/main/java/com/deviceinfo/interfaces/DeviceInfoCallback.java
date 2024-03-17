package com.deviceinfo.interfaces;

public interface DeviceInfoCallback<T> {
    void onSuccess(T response);
    void onError(Exception e);
}
