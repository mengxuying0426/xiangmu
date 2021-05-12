package com.example.yanxiaopeidemo.Bean;

import java.util.List;

public class MissionInfo {
    private List<Mission> missionList;

    public List<Mission> getMissionList() {
        return missionList;
    }

    public void setMissionList(List<Mission> missionList) {
        this.missionList = missionList;
    }

    @Override
    public String toString() {
        return "MissionInfo{" +
                "missionList=" + missionList +
                '}';
    }
}
