package com.example.yanxiaopeidemo.beans;

public class QuestionBank {
    private int id;
    private String username;
    private String kemu;
    private int tinum;
    private int kemuSta;

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

    public int getTinum() {
        return tinum;
    }

    public void setTinum(int tinum) {
        this.tinum = tinum;
    }

    public int getKemuSta() {
        return kemuSta;
    }

    public void setKemuSta(int kemuSta) {
        this.kemuSta = kemuSta;
    }

    public QuestionBank() {
    }

    public QuestionBank(String kemu, int tinum, int kemuSta) {
        this.kemu = kemu;
        this.tinum = tinum;
        this.kemuSta = kemuSta;
    }

    public QuestionBank(int id, String username, String kemu, int tinum, int kemuSta) {
        this.id = id;
        this.username = username;
        this.kemu = kemu;
        this.tinum = tinum;
        this.kemuSta = kemuSta;
    }

    @Override
    public String toString() {
        return "QuestionBank{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", kemu='" + kemu + '\'' +
                ", tinum=" + tinum +
                ", kemuSta=" + kemuSta +
                '}';
    }
}
