package com.example.yanxiaopeidemo.beans;

import java.util.List;

public class ChoiceInfo {
    List<SingleChoice> singleChoices;

    public List<SingleChoice> getSingleChoices() {
        return singleChoices;
    }

    public void setSingleChoices(List<SingleChoice> singleChoices) {
        this.singleChoices = singleChoices;
    }

    @Override
    public String toString() {
        return "ChoiceInfo [singleChoices=" + singleChoices + "]";
    }
}
