package com.example.yanxiaopeidemo.beans;

import java.util.List;

public class WBankInfo {
    List<WrongSet> wrongSets;

    public List<WrongSet> getWrongSets() {
        return wrongSets;
    }

    public void setWrongSets(List<WrongSet> wrongSets) {
        this.wrongSets = wrongSets;
    }

    public WBankInfo(List<WrongSet> wrongSets) {
        this.wrongSets = wrongSets;
    }

    public WBankInfo() {
    }

    @Override
    public String toString() {
        return "WBankInfo{" +
                "wrongSets=" + wrongSets +
                '}';
    }
}
