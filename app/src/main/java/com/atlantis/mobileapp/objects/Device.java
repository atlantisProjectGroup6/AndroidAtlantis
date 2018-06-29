package com.atlantis.mobileapp.objects;

public class Device {

    private String mac;
    private String name;
    private int type;

    public Device(String mac, String name, int type){
        this.mac = mac;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
