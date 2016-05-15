package com.example.justinchou.cheyilian.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by Justin Chou on 2016/4/18.
 * Obd device.
 */
public class Obd extends BaseObservable {

    private String deviceName;
    private String deviceNumber;
    private String rotatingSpeed;
    private String targetRotatingSpeed;
    private String carSpeed;
    private String targetCarSpeed;
    private String throttlingValue;
    private String targetThrottlingValue;

    @Bindable
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Bindable
    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @Bindable
    public String getRotatingSpeed() {
        return rotatingSpeed;
    }

    public void setRotatingSpeed(String rotatingSpeed) {
        this.rotatingSpeed = rotatingSpeed;
    }

    @Bindable
    public String getCarSpeed() {
        return carSpeed;
    }

    public void setCarSpeed(String carSpeed) {
        this.carSpeed = carSpeed;
    }

    @Bindable
    public String getThrottlingValue() {
        return throttlingValue;
    }

    public void setThrottlingValue(String throttlingValue) {
        this.throttlingValue = throttlingValue;
    }
}
