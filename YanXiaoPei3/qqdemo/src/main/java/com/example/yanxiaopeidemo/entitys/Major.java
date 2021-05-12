package com.example.yanxiaopeidemo.entitys;

public class Major {
    private String name;
    private String dm;
    private String predm;//一级学科代码

    public Major(String name, String dm, String predm) {
        this.name = name;
        this.dm = dm;
        this.predm = predm;
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
    public String getPredm() {
        return predm;
    }
    public void setPredm(String predm) {
        this.predm = predm;
    }
}
