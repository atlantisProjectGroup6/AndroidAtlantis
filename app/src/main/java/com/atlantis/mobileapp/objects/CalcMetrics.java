package com.atlantis.mobileapp.objects;

public class CalcMetrics {
    private String deviceMac;
    private float monthAvg;
    private float monthMin;
    private float monthMax;
    private float weekAvg;
    private float weekMin;
    private float weekMax;
    private float dayAvg;
    private float dayMin;
    private float dayMax;

    public CalcMetrics(){

    }

    public CalcMetrics(String deviceMac, float monthAvg, float monthMin, float monthMax, float weekAvg, float weekMin, float weekMax, float dayAvg, float dayMin, float dayMax) {
        this.deviceMac = deviceMac;
        this.monthAvg = monthAvg;
        this.monthMin = monthMin;
        this.monthMax = monthMax;
        this.weekAvg = weekAvg;
        this.weekMin = weekMin;
        this.weekMax = weekMax;
        this.dayAvg = dayAvg;
        this.dayMin = dayMin;
        this.dayMax = dayMax;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public float getMonthAvg() {
        return monthAvg;
    }

    public void setMonthAvg(float monthAvg) {
        this.monthAvg = monthAvg;
    }

    public float getMonthMin() {
        return monthMin;
    }

    public void setMonthMin(float monthMin) {
        this.monthMin = monthMin;
    }

    public float getMonthMax() {
        return monthMax;
    }

    public void setMonthMax(float monthMax) {
        this.monthMax = monthMax;
    }

    public float getWeekAvg() {
        return weekAvg;
    }

    public void setWeekAvg(float weekAvg) {
        this.weekAvg = weekAvg;
    }

    public float getWeekMin() {
        return weekMin;
    }

    public void setWeekMin(float weekMin) {
        this.weekMin = weekMin;
    }

    public float getWeekMax() {
        return weekMax;
    }

    public void setWeekMax(float weekMax) {
        this.weekMax = weekMax;
    }

    public float getDayAvg() {
        return dayAvg;
    }

    public void setDayAvg(float dayAvg) {
        this.dayAvg = dayAvg;
    }

    public float getDayMin() {
        return dayMin;
    }

    public void setDayMin(float dayMin) {
        this.dayMin = dayMin;
    }

    public float getDayMax() {
        return dayMax;
    }

    public void setDayMax(float dayMax) {
        this.dayMax = dayMax;
    }
}
