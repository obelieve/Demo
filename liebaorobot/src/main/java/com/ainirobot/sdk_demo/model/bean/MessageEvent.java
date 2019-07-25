package com.ainirobot.sdk_demo.model.bean;

public class MessageEvent {
    private int fragmentType ;
    private String content;
    public MessageEvent(int fragmentType) {
        this.fragmentType = fragmentType;
    }

    public int getFragmentType() {
        return fragmentType;
    }

    public String getWhat() {
        return content;
    }

    public void setWhat(String what) {
        this.content = what;
    }
}
