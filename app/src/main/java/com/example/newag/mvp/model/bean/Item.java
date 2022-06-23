package com.example.newag.mvp.model.bean;

public class Item {
    private String num;
    private String reason;
    private String time;
    private String name;

    public Item() {
    }

    public Item(String num, String reason, String time, String name) {
        this.num = num;
        this.reason = reason;
        this.time = time;
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
