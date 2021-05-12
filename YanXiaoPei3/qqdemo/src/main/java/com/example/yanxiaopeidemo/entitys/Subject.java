package com.example.yanxiaopeidemo.entitys;

public class Subject {
    private String name;
    private String dm;
    private String ml_dm;


    public Subject(String name, String dm, String ml_dm) {
        this.name = name;
        this.dm = dm;
        this.ml_dm = ml_dm;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDm() {
        return dm;
    }
    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getMl_dm() {
        return ml_dm;
    }
    public void setMl_dm(String ml_dm) {
        this.ml_dm = ml_dm;
    }
    @Override
    public String toString() {
        return "Subject [name=" + name + ", dm=" + dm + ", ml_dm=" + ml_dm + "]";
    }
}
