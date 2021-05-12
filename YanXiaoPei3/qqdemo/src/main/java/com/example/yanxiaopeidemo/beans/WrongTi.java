package com.example.yanxiaopeidemo.beans;

public class WrongTi {
    private int id;
    private String username;
    private int tiid;
    private int tista;
    private String kemu;
    private String stem;
    private String opta;
    private String optb;
    private String optc;
    private String optd;
    private String correct;
    private String analysis;
    private int keynum;
    private int iscollect;

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

    public int getTiid() {
        return tiid;
    }

    public void setTiid(int tiid) {
        this.tiid = tiid;
    }

    public int getTista() {
        return tista;
    }

    public void setTista(int tista) {
        this.tista = tista;
    }

    public String getKemu() {
        return kemu;
    }

    public void setKemu(String kemu) {
        this.kemu = kemu;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getOpta() {
        return opta;
    }

    public void setOpta(String opta) {
        this.opta = opta;
    }

    public String getOptb() {
        return optb;
    }

    public void setOptb(String optb) {
        this.optb = optb;
    }

    public String getOptc() {
        return optc;
    }

    public void setOptc(String optc) {
        this.optc = optc;
    }

    public String getOptd() {
        return optd;
    }

    public void setOptd(String optd) {
        this.optd = optd;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public int getKeynum() {
        return keynum;
    }

    public void setKeynum(int keynum) {
        this.keynum = keynum;
    }

    public int getIscollect() {
        return iscollect;
    }

    public void setIscollect(int iscollect) {
        this.iscollect = iscollect;
    }

    public WrongTi() {
    }

    public WrongTi(int id, String username, int tiid, int tista, String kemu, String stem, String opta, String optb, String optc, String optd, String correct, String analysis, int keynum) {
        this.id = id;
        this.username = username;
        this.tiid = tiid;
        this.tista = tista;
        this.kemu = kemu;
        this.stem = stem;
        this.opta = opta;
        this.optb = optb;
        this.optc = optc;
        this.optd = optd;
        this.correct = correct;
        this.analysis = analysis;
        this.keynum = keynum;
    }

    public WrongTi(int id, String username, int tiid, int tista, String kemu, String stem, String opta, String optb, String optc, String optd, String correct, String analysis, int keynum, int iscollect) {
        this.id = id;
        this.username = username;
        this.tiid = tiid;
        this.tista = tista;
        this.kemu = kemu;
        this.stem = stem;
        this.opta = opta;
        this.optb = optb;
        this.optc = optc;
        this.optd = optd;
        this.correct = correct;
        this.analysis = analysis;
        this.keynum = keynum;
        this.iscollect = iscollect;
    }

    @Override
    public String toString() {
        return "WrongTi{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", tiid=" + tiid +
                ", tista=" + tista +
                ", kemu='" + kemu + '\'' +
                ", stem='" + stem + '\'' +
                ", opta='" + opta + '\'' +
                ", optb='" + optb + '\'' +
                ", optc='" + optc + '\'' +
                ", optd='" + optd + '\'' +
                ", correct='" + correct + '\'' +
                ", analysis='" + analysis + '\'' +
                ", keynum=" + keynum +
                ", iscollect=" + iscollect +
                '}';
    }
}
