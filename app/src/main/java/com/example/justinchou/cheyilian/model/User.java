package com.example.justinchou.cheyilian.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by Justin Chou on 2016/4/17.
 */
public class User extends BaseObservable {

    private String deviceNumber;
    private String userName;
    private String phoneNumber;

    @Bindable
    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
