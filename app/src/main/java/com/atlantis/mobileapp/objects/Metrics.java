package com.atlantis.mobileapp.objects;

public class Metrics {
    private int id;
    private String value;
    private long date;

    public Metrics(int id, String val, long date)
    {
        this.id = id;
        this.value = val;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}

