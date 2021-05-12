package com.example.yanxiaopeidemo.Bean;

public class Mission {
    String event;
    String finish;
    String endwork;

    public Mission(String event,String finish,String endwork) {
        this.event = event;
        this.finish = finish;
        this.endwork = endwork;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getEndwork() {
        return endwork;
    }

    public void setEndwork(String endwork) {
        this.endwork = endwork;
    }
}
