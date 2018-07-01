package com.atlantis.mobileapp.dataaccess;

import com.atlantis.mobileapp.objects.Device;
import com.atlantis.mobileapp.objects.Metrics;

import java.util.ArrayList;

public interface ClientWSCallBack {
    void endGetError(String error);
    void endSendUserId(String response);
    void endSendUserName(String response);
    void endGetUserDevices(ArrayList<Device> devices);
    void endGetLatestMetrics(ArrayList<Metrics> metrics);
}
