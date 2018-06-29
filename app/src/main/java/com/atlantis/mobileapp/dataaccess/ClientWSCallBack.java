package com.atlantis.mobileapp.dataaccess;

import com.atlantis.mobileapp.objects.Device;

import java.util.ArrayList;

public interface ClientWSCallBack {
    void endGetError(String error);
    void endGetUserDevices(ArrayList<Device> devices);
}
