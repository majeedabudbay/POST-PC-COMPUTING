package com.example.bodyguardapp;

public class dbItem {
    public String sessionType;
    public String cals;
    public String time;

    public dbItem() {}
    public dbItem(String cals,String time,String sessionType) {
        this.cals = cals;
        this.time =time;
        this.sessionType = sessionType;
    }
}