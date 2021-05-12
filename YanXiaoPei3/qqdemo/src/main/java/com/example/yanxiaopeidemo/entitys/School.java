package com.example.yanxiaopeidemo.entitys;

public class School {
    private int img;
    private String name;
    private String location;
    private String tag;
    private String type;
    private String path;

    public School() {
    }

    public School(int img, String name, String location, String tag, String type, String path) {
        this.img = img;
        this.name = name;
        this.location = location;
        this.tag = tag;
        this.type = type;
        this.path = path;
    }

    public School(int img, String name, String location, String tag, String type) {
        this.img = img;
        this.name = name;
        this.location = location;
        this.tag = tag;
        this.type = type;
    }

    public School(String name, String location, String tag, String type) {
        this.name = name;
        this.location = location;
        this.tag = tag;
        this.type = type;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "School{" +
                "img=" + img +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", tag='" + tag + '\'' +
                ", type='" + type + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
