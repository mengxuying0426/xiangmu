package com.example.yanxiaopeidemo.Activity4;

public class Plan {
    private String name;
    private String time;
    private String pro;
    private int img;

    public Plan(String name, String time, String pro, int img) {
        this.name = name;
        this.time = time;
        this.pro = pro;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
