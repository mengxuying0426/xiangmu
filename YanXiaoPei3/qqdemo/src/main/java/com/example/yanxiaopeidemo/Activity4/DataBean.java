package com.example.yanxiaopeidemo.Activity4;

public class DataBean {
    private String date;
    private String describe;

    public DataBean(String date, String describe) {
        this.date = date;
        this.describe = describe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
