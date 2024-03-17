package com.deviceinfo.util;

import com.deviceinfo.model.DeviceInfoResult;

public class DIResult {
    private DeviceInfoResult result;
    private Exception exception;

    public DIResult(DeviceInfoResult result) {
        this.result = result;
    }

    public DIResult(Exception exception) {
        this.exception = exception;
    }

    public DeviceInfoResult getResult() {
        return result;
    }

    public DIResult setResult(DeviceInfoResult result) {
        this.result = result;
        return this;
    }

    public Exception getException() {
        return exception;
    }

    public DIResult setException(Exception exception) {
        this.exception = exception;
        return this;
    }
}
