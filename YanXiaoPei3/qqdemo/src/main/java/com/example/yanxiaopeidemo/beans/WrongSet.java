package com.example.yanxiaopeidemo.beans;

public class WrongSet {
    private int id;
    private String username;
    private String kemu;
    private int kemusta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKemu() {
        return kemu;
    }

    public void setKemu(String kemu) {
        this.kemu = kemu;
    }

    public int getKemusta() {
        return kemusta;
    }

    public void setKemusta(int kemusta) {
        this.kemusta = kemusta;
    }

    public WrongSet(int id, String username, String kemu, int kemusta) {
        this.id = id;
        this.username = username;
        this.kemu = kemu;
        this.kemusta = kemusta;
    }

    public WrongSet() {
    }

    @Override
    public String toString() {
        return "WrongSet{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", kemu='" + kemu + '\'' +
                ", kemusta=" + kemusta +
                '}';
    }
}
