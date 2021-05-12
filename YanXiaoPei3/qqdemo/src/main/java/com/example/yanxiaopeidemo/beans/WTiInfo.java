package com.example.yanxiaopeidemo.beans;

import java.util.List;

public class WTiInfo {
    List<WrongTi> wrongTis;

    public WTiInfo() {
    }

    public WTiInfo(List<WrongTi> wrongTis) {
        this.wrongTis = wrongTis;
    }

    public List<WrongTi> getWrongTis() {
        return wrongTis;
    }

    public void setWrongTis(List<WrongTi> wrongTis) {
        this.wrongTis = wrongTis;
    }

    @Override
    public String toString() {
        return "WTiInfo{" +
                "wrongTis=" + wrongTis +
                '}';
    }
}
