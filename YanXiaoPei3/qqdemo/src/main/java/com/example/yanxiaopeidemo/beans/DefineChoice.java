package com.example.yanxiaopeidemo.beans;

public class DefineChoice {
    private String username;
    private String stem;
    private String answer;
    private String opta;
    private String optb;
    private String optc;
    private String optd;
    private String img;
    private String style;
    private int id;
    private int tista;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTista() {
        return tista;
    }

    public void setTista(int tista) {
        this.tista = tista;
    }

    public DefineChoice(String username, String stem, String answer, String opta, String optb, String optc, String optd, String img, String style, int id, int tista) {
        this.username = username;
        this.stem = stem;
        this.answer = answer;
        this.opta = opta;
        this.optb = optb;
        this.optc = optc;
        this.optd = optd;
        this.img = img;
        this.style = style;
        this.id = id;
        this.tista = tista;
    }

    public DefineChoice() {
    }

    public DefineChoice(String username, String stem, String answer, String opta, String optb, String optc, String optd, String style, int id, int tista) {
        this.username = username;
        this.stem = stem;
        this.answer = answer;
        this.opta = opta;
        this.optb = optb;
        this.optc = optc;
        this.optd = optd;
        this.style = style;
        this.id = id;
        this.tista = tista;
    }

    public DefineChoice(String username, String answer, String img, String style, int id, int tista) {
        this.username = username;
        this.answer = answer;
        this.img = img;
        this.style = style;
        this.id = id;
        this.tista = tista;
    }

    @Override
    public String toString() {
        return "DefineChoice{" +
                "username='" + username + '\'' +
                ", stem='" + stem + '\'' +
                ", answer='" + answer + '\'' +
                ", opta='" + opta + '\'' +
                ", optb='" + optb + '\'' +
                ", optc='" + optc + '\'' +
                ", optd='" + optd + '\'' +
                ", img='" + img + '\'' +
                ", style='" + style + '\'' +
                ", id=" + id +
                ", tista=" + tista +
                '}';
    }
}
