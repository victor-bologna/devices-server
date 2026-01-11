package com.bologna.devices.exceptions;

public class DeviceException extends RuntimeException {
    private final int httpCode;
    public DeviceException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
