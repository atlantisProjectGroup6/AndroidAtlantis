package com.atlantis.mobileapp.objects;

public class Device {

    private int id;
    private String mac;
    private String name;
    private SensorType type;

    public Device(int id, String mac, String name, SensorType type){
        this.id = id;
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

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public SensorType getType() {
        return type;
    }
    public void setType(SensorType type) {
        this.type = type;
    }
}
