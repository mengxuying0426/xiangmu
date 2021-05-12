package com.example.yanxiaopeidemo.beans;

public class SingleChoice {
    private String username;
    private String kemu;
    private int tiid;
    private String stem;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correct;
    private int keyNum;
    private String analysis;
    private int motista;
    private int iscollect;
    private int iscuoti;


    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
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

    public int getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(int keyNum) {
        this.keyNum = keyNum;
    }

    public int getTiid() {
        return tiid;
    }

    public void setTiid(int tiid) {
        this.tiid = tiid;
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

    public int getMotista() {
        return motista;
    }

    public void setMotista(int motista) {
        this.motista = motista;
    }

    public int getIscollect() {
        return iscollect;
    }

    public void setIscollect(int iscollect) {
        this.iscollect = iscollect;
    }

    public int getIscuoti() {
        return iscuoti;
    }

    public void setIscuoti(int iscuoti) {
        this.iscuoti = iscuoti;
    }

    public SingleChoice() {
    }

    public SingleChoice(String username, String kemu, int tiid, String stem, String optionA, String optionB, String optionC, String optionD, String correct, int keyNum, String analysis, int motista) {
        this.username = username;
        this.kemu = kemu;
        this.tiid = tiid;
        this.stem = stem;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correct = correct;
        this.keyNum = keyNum;
        this.analysis = analysis;
        this.motista = motista;
    }

    public SingleChoice(String stem, String optionA, String optionB, String optionC, String optionD, String correct, String analysis, int keyNum) {
        this.stem = stem;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correct = correct;
        this.analysis = analysis;
        this.keyNum = keyNum;
    }

    public SingleChoice(String stem, String optionA, String optionB, String optionC, String optionD, String correct, int keyNum, String analysis) {
        this.stem = stem;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correct = correct;
        this.keyNum = keyNum;
        this.analysis = analysis;
    }

    public SingleChoice(String username, String kemu, int tiid, String stem, String optionA, String optionB, String optionC, String optionD, String correct, int keyNum, String analysis, int motista, int iscollect, int iscuoti) {
        this.username = username;
        this.kemu = kemu;
        this.tiid = tiid;
        this.stem = stem;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correct = correct;
        this.keyNum = keyNum;
        this.analysis = analysis;
        this.motista = motista;
        this.iscollect = iscollect;
        this.iscuoti = iscuoti;
    }
    @Override
    public String toString() {
        return "SingleChoice{" +
                "username='" + username + '\'' +
                ", kemu='" + kemu + '\'' +
                ", tiid=" + tiid +
                ", stem='" + stem + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ", correct='" + correct + '\'' +
                ", keyNum=" + keyNum +
                ", analysis='" + analysis + '\'' +
                ", motista=" + motista +
                ", iscollect=" + iscollect +
                ", iscuoti=" + iscuoti +
                '}';
    }
}
