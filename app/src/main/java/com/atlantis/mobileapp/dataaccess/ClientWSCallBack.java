package com.atlantis.mobileapp.dataaccess;

public interface ClientWSCallBack {
    void endGetWeather(String name,String main,String description);
    void endGetError(String error);
}
